package com.java.service;

import com.java.common.ResponseResult;
import com.java.entity.JobLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 定时任务调度日志表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-08
 */
public interface JobLogService extends IService<JobLog> {

    ResponseResult listJobLog(String jobName, String jobGroup, String status, String startTime,
                              String endTime, Long jobId);

    ResponseResult deleteBatch(List<Long> ids);

    ResponseResult clean();
}
