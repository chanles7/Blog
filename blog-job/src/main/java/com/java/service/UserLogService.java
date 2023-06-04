package com.java.service;

import com.java.common.ResponseResult;
import com.java.entity.UserLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-11-09
 */
public interface UserLogService extends IService<UserLog> {

    ResponseResult listUserLog();

    ResponseResult deleteBatch(List<Long> ids);
}
