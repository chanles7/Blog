package com.java.strategy.imp;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.java.common.RedisConstants;
import com.java.common.ResultCode;
import com.java.entity.Role;
import com.java.entity.User;
import com.java.enums.LoginTypeEnum;
import com.java.enums.UserStatusEnum;
import com.java.mapper.RoleMapper;
import com.java.mapper.UserMapper;
import com.java.service.RedisService;
import com.java.strategy.SocialLoginStrategy;
import com.java.util.BeanCopyUtils;
import com.java.util.IpUtils;
import com.java.vo.SocialTokenVO;
import com.java.vo.SocialUserInfoVO;
import com.java.vo.UserDetailVO;
import com.java.vo.UserInfoVO;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 第三方登录抽象模板
 *
 * @author blue
 * @date 2021/07/28
 */
@Service
public abstract class AbstractSocialLoginStrategyImpl implements SocialLoginStrategy {
    @Autowired
    private UserMapper userMapper;
    @Resource
    private HttpServletRequest request;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public UserInfoVO login(String data) {
        // 创建登录信息
        UserDetailVO userDetailVO;
        // 获取第三方token信息
        SocialTokenVO socialToken = getSocialToken(data);
        // 获取用户ip信息
        String ipAddress = IpUtils.getIp(request);
        String ipSource = IpUtils.getCityInfo(ipAddress);
        // 获取第三方用户信息
        SocialUserInfoVO socialUserInfo = getSocialUserInfo(socialToken);
        if (socialToken.getLoginType().equals(LoginTypeEnum.GITEE.getType())){
            socialToken.setOpenId(socialUserInfo.getId());
        }
        // 判断是否已注册
        User user = getUser(socialToken);
        if (Objects.nonNull(user)) {
            // 返回数据库用户信息
            userDetailVO = getUserDetail(user, ipAddress, ipSource,socialUserInfo);
        } else {
            // 获取第三方用户信息，保存到数据库返回
//            userDetailVO = saveUserDetail(socialToken, ipAddress, ipSource,socialUserInfo);
            userDetailVO = null;
        }
        // 判断账号是否禁用
        Assert.isTrue(!userDetailVO.getIsDisable().equals(UserStatusEnum.disable.code), ResultCode.DISABLE_ACCOUNT.desc);

        // 返回用户信息
        UserInfoVO userInfoVO = BeanCopyUtils.copyObject(userDetailVO, UserInfoVO.class);
        StpUtil.login(userInfoVO.getId().longValue());
        userInfoVO.setToken(StpUtil.getTokenValue());
        return userInfoVO;
    }

    /**
     * 获取第三方token信息
     *
     * @param data 数据
     * @return {@link SocialTokenVO} 第三方token信息
     */
    public abstract SocialTokenVO getSocialToken(String data);

    /**
     * 获取第三方用户信息
     *
     * @param socialTokenVO 第三方token信息
     * @return {@link SocialUserInfoVO} 第三方用户信息
     */
    public abstract SocialUserInfoVO getSocialUserInfo(SocialTokenVO socialTokenVO);

    /**
     * 获取用户账号
     *
     * @return {@link socialTokenVO} 用户账号
     */
    private User getUser(SocialTokenVO socialTokenVO) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, socialTokenVO.getOpenId())
                .eq(User::getLoginType, socialTokenVO.getLoginType()));
    }

    /**
     * 获取用户信息
     *
     * @param user      用户账号
     * @param ipAddress ip地址
     * @param ipSource  ip源
     * @return {@link UserDetailVO} 用户信息
     */
    private UserDetailVO getUserDetail(User user, String ipAddress, String ipSource, SocialUserInfoVO socialUserInfo) {
        // 更新登录信息
        userMapper.update(new User(), new LambdaUpdateWrapper<User>()
                .set(User::getIpAddress, ipAddress)
                .set(User::getIpSource, ipSource)
                .eq(User::getId, user.getId()));

        //更新头像和昵称
//        userAuthMapper.update(new UserAuth(),new LambdaUpdateWrapper<UserAuth>()
//                .set(UserAuth::getAvatar, socialUserInfo.getAvatar())
//                .set(UserAuth::getNickname, socialUserInfo.getNickname())
//                .eq(UserAuth::getId, user.getUserAuthId()));

        // 封装信息
        return convertUserDetail(user);
    }



    private UserDetailVO convertUserDetail(User user) {
        // 查询账号信息
        User use = userMapper.selectById(user.getId());
        // 查询账号点赞信息
        Set<Object> articleLikeSet = redisService.sMembers(RedisConstants.ARTICLE_USER_LIKE + user.getId());
        // 获取设备信息
        String ipAddress = IpUtils.getIp(request);
        String ipSource = IpUtils.getCityInfo(ipAddress);
        UserAgent userAgent = IpUtils.getUserAgent(request);
        // 查询账号角色
        Role role = roleMapper.selectById(user.getRoleId());
        List<String> roleList = new ArrayList<>();
        roleList.add(role.getCode());
        // 封装权限集合
        return UserDetailVO.builder()
                .id(user.getId())
                .loginType(user.getLoginType())
                .username(user.getUsername())
                .password(user.getPassword())
                .roleList(roleList)
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .articleLikeSet(articleLikeSet)
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .isDisable(user.getStatus())
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .lastLoginTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")))
                .build();
    }
}
