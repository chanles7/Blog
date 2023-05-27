package com.java.kael.strategy.imp;

import com.java.kael.common.ResultCode;
import com.java.kael.common.SocialLoginConst;
import com.java.kael.config.properties.GiteeConfigProperties;
import com.java.kael.enums.LoginTypeEnum;
import com.java.kael.exception.BusinessException;
import com.java.kael.vo.GiteeUserInfoVO;
import com.java.kael.vo.SocialTokenVO;
import com.java.kael.vo.SocialUserInfoVO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 码云登录策略实现
 *
 * @author blue
 * @date 2021/07/28
 */
@Service("gitEELoginStrategyImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GitEELoginStrategyImpl extends AbstractSocialLoginStrategyImpl {

    private static final Logger logger = LoggerFactory.getLogger(GitEELoginStrategyImpl.class);

    private final RestTemplate restTemplate;

    private final GiteeConfigProperties giteeConfigProperties;

    @Override
    public SocialTokenVO getSocialToken(String code) {
        // 获取码云token信息
        String token  = getGitEEToken(code);
        logger.info("GitEE login as accessToken :{}",token);
        // 返回token信息
        return SocialTokenVO.builder()
                .openId(null)
                .accessToken(token)
                .loginType(LoginTypeEnum.GITEE.getType())
                .build();
    }

    @Override
    public SocialUserInfoVO getSocialUserInfo(SocialTokenVO socialTokenVO) {
        // 定义请求参数
        Map<String, String> data = new HashMap<>(1);
        data.put(SocialLoginConst.ACCESS_TOKEN, socialTokenVO.getAccessToken());
        // 获取码云用户信息
        GiteeUserInfoVO giteeUserInfoVO = restTemplate.getForObject(giteeConfigProperties.getUserInfoUrl(), GiteeUserInfoVO.class, data);
        logger.info("GitEE login as info :{}", giteeUserInfoVO.toString());
        // 返回用户信息
        return SocialUserInfoVO.builder()
                .id(Objects.requireNonNull(giteeUserInfoVO).getId())
                .nickname(Objects.requireNonNull(giteeUserInfoVO).getName())
                .avatar(giteeUserInfoVO.getAvatar_url())
                .build();
    }

    /**
     * 获取码云token信息
     *
     * @param code 码云code
     */
    private String getGitEEToken(String code) {

        // 根据code换取accessToken
        MultiValueMap<String, String> gitEEData = new LinkedMultiValueMap<>();
        // 定义微博token请求参数
        gitEEData.add(SocialLoginConst.CLIENT_ID, giteeConfigProperties.getAppId());
        gitEEData.add(SocialLoginConst.CLIENT_SECRET, giteeConfigProperties.getAppSecret());
        gitEEData.add(SocialLoginConst.GRANT_TYPE, giteeConfigProperties.getGrantType());
        gitEEData.add(SocialLoginConst.REDIRECT_URI, giteeConfigProperties.getRedirectUrl());
        gitEEData.add(SocialLoginConst.CODE, code);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(gitEEData, null);
        try {
            Map map  = restTemplate.exchange(giteeConfigProperties.getAccessTokenUrl(), HttpMethod.POST, requestEntity, Map.class).getBody();
            String accessToken =map.get("access_token").toString();
            return accessToken;
        } catch (Exception e) {
            throw new BusinessException(ResultCode.GITEE_LOGIN_ERROR.getDesc());
        }
    }

}

