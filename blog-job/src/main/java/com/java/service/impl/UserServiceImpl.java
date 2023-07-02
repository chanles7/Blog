package com.java.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.common.RedisConstants;
import com.java.common.ResponseResult;
import com.java.common.ResultCode;
import com.java.config.satoken.MySaTokenListener;
import com.java.config.satoken.OnlineUser;
import com.java.dto.LoginDTO;
import com.java.dto.RegisterDTO;
import com.java.dto.UserInfoDTO;
import com.java.entity.BlogArticle;
import com.java.entity.Category;
import com.java.entity.Menu;
import com.java.entity.User;
import com.java.enums.UserStatusEnum;
import com.java.mapper.UserMapper;
import com.java.service.*;
import com.java.util.AesEncryptUtils;
import com.java.util.PageUtils;
import com.java.util.UserHolder;
import com.java.vo.SystemUserVO;
import com.java.vo.UserInfoVO;
import com.java.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author blue
 * @description:
 * @date 2021/7/30 17:25
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final MenuService menuService;

    private final RedisService redisService;

    private final UserHolder userHolder;

    private final ArticleService articleService;

    private final CategoryService categoryService;

    private final WebConfigService webConfigService;

    /**
     * 用户列表
     *
     * @param username
     * @param loginType
     * @return
     */
    @Override
    public ResponseResult listUser(String username, Integer loginType) {
        Page<UserVO> page = baseMapper.selectPageRecord(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), username, loginType);
        return ResponseResult.success(page);
    }

    /**
     * 用户详情
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult getUserById(Integer id) {
        SystemUserVO user = baseMapper.getById(id);
        return ResponseResult.success(user);
    }

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertUser(User user) {
        user.setPassword(AesEncryptUtils.aesEncrypt(user.getPassword()));
        user.setStatus(UserStatusEnum.normal.code);
        baseMapper.insert(user);
        // roleMapper.insertToUserId(user.getId(),user.getRoleId());
        return ResponseResult.success(user);
    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateUser(User user) {
        baseMapper.updateById(user);
        //roleMapper.updateByUserId(user.getId(),user.getRoleId());
        return ResponseResult.success();
    }

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBatch(List<Integer> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ? ResponseResult.success() : ResponseResult.error("删除失败");
    }

    /**
     * 获取当前登录用户详情
     *
     * @return
     */
    @Override
    public ResponseResult getCurrentUserInfo() {
        return ResponseResult.success("获取当前登录用户信息成功", baseMapper.getById(StpUtil.getLoginIdAsInt()));
    }

    /**
     * 获取当前登录用户所拥有的菜单权限
     *
     * @return
     */
    @Override
    public ResponseResult getCurrentUserMenu() {
        List<Integer> menuIds = baseMapper.getMenuId(StpUtil.getLoginIdAsInt());
        List<Menu> menus = menuService.listByIds(menuIds);
        List<Menu> menuTree = menuService.listMenuTree(menus);
        return ResponseResult.success(menuTree);
    }

    /**
     * 修改密码
     *
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updatePassword(Map<String, String> map) {

        User user = baseMapper.selectById(StpUtil.getLoginIdAsInt());
        Assert.notNull(user, ResultCode.ERROR_USER_NOT_EXIST.getDesc());

        boolean isValid = AesEncryptUtils.validate(user.getPassword(), map.get("oldPassword"));
        Assert.isTrue(isValid, "旧密码校验不通过!");

        String newPassword = AesEncryptUtils.aesEncrypt(map.get("newPassword"));
        user.setPassword(newPassword);
        baseMapper.updateById(user);
        return ResponseResult.success("修改成功");
    }

    /**
     * 在线用户
     *
     * @param keywords
     * @return
     */
    @Override
    public ResponseResult listOnlineUsers(String keywords) {
        int pageNo = PageUtils.getPageNo().intValue();
        int pageSize = PageUtils.getPageSize().intValue();

        List<OnlineUser> onlineUsers = MySaTokenListener.ONLINE_USERS;
        //根据关键词过滤
        if (StringUtils.isNotBlank(keywords)) {
            onlineUsers = MySaTokenListener.ONLINE_USERS.stream().filter(item -> item.getNickname().contains(keywords)).collect(Collectors.toList());
        }
        //排序
        onlineUsers.sort((o1, o2) -> DateUtil.compare(o2.getLoginTime(), o1.getLoginTime()));
        int fromIndex = (pageNo - 1) * pageSize;
        int toIndex = onlineUsers.size() - fromIndex > pageSize ? fromIndex + pageSize : onlineUsers.size();
        List<OnlineUser> userOnlineList = onlineUsers.subList(fromIndex, toIndex);
        logger.info("memory用户数：{}", userOnlineList.size());

        Map<String, Object> map = new HashMap<>();
        map.put("total", onlineUsers.size());
        map.put("records", userOnlineList);
        return ResponseResult.success(map);
    }

    /**
     * 踢人下线
     *
     * @param token
     * @return
     */
    @Override
    public ResponseResult kick(String token) {
        logger.info("当前踢下线的用户token为:{}", token);
        StpUtil.kickoutByTokenValue(token);
        return ResponseResult.success();
    }


    /**
     * 账号注册
     *
     * @param register
     * @return
     */
    @Override
    public ResponseResult register(RegisterDTO register) {
        String username = register.getUsername();
        if (!username.matches("^[a-zA-Z]{4,10}$")) {
            return ResponseResult.error(ResultCode.ERROR_DEFAULT.getDesc());
        }
        String password = register.getPassword().trim();
        if (password.length() < 6) {
            return ResponseResult.error(ResultCode.ERROR_DEFAULT.getDesc());
        }

        User user = this.query().eq("username", username).one();
        if (user != null) {
            return ResponseResult.error(ResultCode.ERROR_DEFAULT.getDesc());
        }
        user = User.builder()
                .username(username)
                .password(AesEncryptUtils.aesEncrypt(password))
                .nickname("游客" + System.currentTimeMillis())
                .avatar(webConfigService.query().list().get(0).getTouristAvatar())
                .build();
        boolean insert = this.save(user);

        return insert ? ResponseResult.success("注册成功") : ResponseResult.error(ResultCode.ERROR_DEFAULT.getDesc());
    }


    @Override
    public ResponseResult login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        User user = this.query().eq("username", username).one();
        if (user == null) {
            return ResponseResult.error(ResultCode.ERROR_MUST_REGISTER.desc);
        }
        boolean validate = AesEncryptUtils.validate(user.getPassword(), loginDTO.getPassword());
        Assert.isTrue(validate, ResultCode.ERROR_PASSWORD.getDesc());

        //登录
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        logger.info("token:{}", token);

        redisService.setCacheObject(RedisConstants.USER_TOKEN + token, user.getId(), 7, TimeUnit.DAYS);

        //组装数据
        UserInfoVO userInfoVO = UserInfoVO.builder()
                .avatar(user.getAvatar())
                .nickname(user.getNickname())
                .token(token)
                .build();

        return ResponseResult.success(userInfoVO);
    }


    @Override
    public ResponseResult getUserInfo() {
        User user = userHolder.getCurrentUser();
        return ResponseResult.success(getUserInfo(user.getId()));
    }


    @Override
    public ResponseResult getAuthorInfo() {
        return ResponseResult.success(getUserInfo(1L));
    }


    private UserInfoVO getUserInfo(Long uid) {
        User user = this.getById(uid);
        List<BlogArticle> blogs = articleService.getBlogsByUserId(user.getId());
        int blogCount = 0;
        int blogViews = 0;
        for (BlogArticle blog : blogs) {
            blogCount++;
            blogViews += blog.getQuantity();
        }
        List<Category> categories = categoryService.listByUid(uid);
        return UserInfoVO.builder()
                .avatar(user.getAvatar())
                .nickname(user.getNickname())
                .blogCount(blogCount)
                .blogViews(blogViews)
                .categoryCount(categories.size())
                .build();
    }


    @Override
    public ResponseResult tokenRecharge(HttpServletRequest request) {
        User currentUser = userHolder.getCurrentUser();
        if (currentUser == null) {
            return ResponseResult.error(ResultCode.NOT_LOGIN.getCode(), ResultCode.NOT_LOGIN.getDesc());
        }
        String token = request.getHeader("X-Token");
        redisService.setCacheObject(RedisConstants.USER_TOKEN + token, currentUser.getId(), 30, TimeUnit.MINUTES);
        return ResponseResult.success();
    }


    @Override
    public ResponseResult update(UserInfoDTO userInfoVO) {
        boolean update = this.update().eq("id", userHolder.getCurrentUid())
                .set("nickname", userInfoVO.getNickname())
                .set("avatar", userInfoVO.getAvatar())
                .update();
        return update ? ResponseResult.success() : ResponseResult.error();
    }
}
