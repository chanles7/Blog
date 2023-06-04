package com.java.mapper;

import com.java.entity.JobLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 定时任务调度日志表 Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-12-08
 */
@Mapper
public interface JobLogMapper extends BaseMapper<JobLog> {

    void clean();

}
