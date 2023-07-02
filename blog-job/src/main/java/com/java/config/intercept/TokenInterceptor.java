package com.java.config.intercept;

import com.java.common.RedisConstants;
import com.java.service.RedisService;
import com.java.util.UserHolder;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.concurrent.TimeUnit;

import static com.java.common.RedisConstants.USER_TOKEN;

/**
 * description 功能描述
 *
 * @author ch
 * create 2023/5/31 23:29
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private UserHolder userHolder;

    private RedisService redisService;

    public TokenInterceptor(UserHolder userHolder, RedisService redisService) {
        this.userHolder = userHolder;
        this.redisService = redisService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, @NotNull Object handler) {
        String token = request.getHeader("X-Token");
        if (StringUtils.hasText(token)) {
            Object uid = redisService.getCacheObject(USER_TOKEN + token);
            if (uid instanceof Integer) {
                redisService.expire(RedisConstants.USER_TOKEN + token, 7, TimeUnit.DAYS);
                userHolder.addCurrentUser(((Integer) uid).longValue());
            }
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (userHolder.getCurrentUser() == null) return;
        userHolder.remove();
    }

}
