package com.java.util;

import com.java.entity.User;
import com.java.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * description 功能描述
 *
 * @author ch
 * create 2023/5/31 23:33
 */
@Component
public class UserHolder {

    @Resource
    private UserService userService;

    private final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public void addCurrentUser(Long uid) {
        User user = userService.getById(uid);
        userThreadLocal.set(user);
    }

    public User getCurrentUser() {
        return userThreadLocal.get();
    }


    public Long getCurrentUid() {
        return getCurrentUser().getId();
    }

    public void remove() {
        userThreadLocal.remove();
    }
}
