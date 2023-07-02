package com.java.controller.api;

import com.java.common.ResponseResult;
import com.java.dto.LoginDTO;
import com.java.dto.RegisterDTO;
import com.java.dto.UserInfoDTO;
import com.java.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("/user")
@RestController
@Api(tags = "用户相关接口")
@RequiredArgsConstructor
public class ApiUserController {

    private final UserService userService;


    @PostMapping(value = "/register")
    @ApiOperation(value = "账号注册", httpMethod = "POST", response = ResponseResult.class, notes = "账号注册")
    public ResponseResult register(@Valid @RequestBody RegisterDTO registerDTO){
        return userService.register(registerDTO);
    }


    @PostMapping(value = "/login")
    @ApiOperation(value = "账户登录", httpMethod = "POST", response = ResponseResult.class, notes = "账户登录")
    public ResponseResult login(@Valid @RequestBody LoginDTO loginDTO){
        return userService.login(loginDTO);
    }



    @GetMapping(value = "/info")
    @ApiOperation(value = "用户信息获取", httpMethod = "GET", response = ResponseResult.class, notes = "用户信息获取")
    public ResponseResult getUserInfo(){
        return userService.getUserInfo();
    }


    @GetMapping(value = "/author")
    @ApiOperation(value = "作者信息获取", httpMethod = "GET", response = ResponseResult.class, notes = "作者信息获取")
    public ResponseResult getAuthorInfo(){
        return userService.getAuthorInfo();
    }


    @PostMapping(value = "/token")
    @ApiOperation(value = "续费token", httpMethod = "POST", response = ResponseResult.class, notes = "续费token")
    public ResponseResult tokenRecharge(HttpServletRequest request){
        return userService.tokenRecharge(request);
    }


    @PostMapping(value = "/update")
    @ApiOperation(value = "更新用户信息", httpMethod = "POST", response = ResponseResult.class, notes = "更新用户信息")
    public ResponseResult update(@RequestBody UserInfoDTO userInfoVO){
        return userService.update(userInfoVO);
    }
}

