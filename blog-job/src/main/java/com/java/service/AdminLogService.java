package com.java.service;

import com.java.common.ResponseResult;
import com.java.entity.AdminLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  操作日志服务类
 *
 * @author blue
 * @since 2021-11-10
 */
public interface AdminLogService extends IService<AdminLog> {

    /**
     * 分页查询操作日志
     * @return
     */
    ResponseResult listAdminLog();

    /**
     * 批量删除操作日志
     * @param ids 操作日志id集合
     * @return
     */
    ResponseResult deleteAdminLog(List<Long> ids);
}
