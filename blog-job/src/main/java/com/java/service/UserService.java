package com.java.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.java.common.ResponseResult;
import com.java.dto.LoginDTO;
import com.java.dto.RegisterDTO;
import com.java.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *
 * @author blue
 * @date: 2021/7/30 17:17
 */
public interface UserService extends IService<User> {

    ResponseResult listUser(String username, Integer loginType);

    ResponseResult getUserById(Integer id);

    ResponseResult insertUser(User user);

    ResponseResult updateUser(User user);

    ResponseResult deleteBatch(List<Integer> ids);

    ResponseResult getCurrentUserInfo();

    ResponseResult getCurrentUserMenu();

    ResponseResult updatePassword(Map<String, String> map);

    ResponseResult listOnlineUsers(String keywords);

    ResponseResult kick(String token);

    ResponseResult register(RegisterDTO registerDTO);

    ResponseResult login(LoginDTO loginDTO);

    ResponseResult getUserInfo();

    ResponseResult getAuthorInfo();

    ResponseResult tokenRecharge(HttpServletRequest request);
}
