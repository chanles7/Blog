package com.java.kael.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.java.kael.annotation.OperationLogger;
import com.java.kael.common.ResponseResult;
import com.java.kael.service.UserLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 日志表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-11-09
 */
@RestController
@RequestMapping("/system/userLog")
@RequiredArgsConstructor
@Api(tags = "用户日志管理")
public class UserLogController {

    private final UserLogService userLogService;

    @GetMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "用户日志列表", httpMethod = "GET", response = ResponseResult.class, notes = "用户日志列表")
    public ResponseResult list() {
        return userLogService.listUserLog();
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("/system/userLog/delete")
    @OperationLogger(value = "删除用户日志")
    @ApiOperation(value = "删除用户日志", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除用户日志")
    public ResponseResult deleteBatch(@RequestBody List<Long> ids) {
        return userLogService.deleteBatch(ids);
    }
}

