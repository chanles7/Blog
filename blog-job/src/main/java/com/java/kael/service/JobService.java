package com.java.kael.service;

import com.java.kael.common.ResponseResult;
import com.java.kael.entity.Job;
import com.java.kael.enums.TaskException;
import com.baomidou.mybatisplus.extension.service.IService;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * <p>
 * 定时任务调度表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-08
 */
public interface JobService extends IService<Job> {

    ResponseResult listJob(String jobName, String jobGroup, String status);

    ResponseResult getJobById(Long jobId);

    ResponseResult insertJob(Job job) throws SchedulerException, TaskException;

    ResponseResult updateJob(Job job) throws SchedulerException, TaskException;

    ResponseResult deleteJob(Long jobId) throws SchedulerException;

    ResponseResult deleteBatch(List<Long> ids);

    ResponseResult pauseJob(Job job) throws SchedulerException ;

    ResponseResult run(Job job);

    ResponseResult changeStatus(Job job) throws SchedulerException;

}
