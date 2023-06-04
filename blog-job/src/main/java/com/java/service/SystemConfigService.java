package com.java.service;

import com.java.common.ResponseResult;
import com.java.entity.SystemConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统配置表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
public interface SystemConfigService extends IService<SystemConfig> {

    ResponseResult getConfig();

    ResponseResult updateConfig(SystemConfig systemConfig);

    SystemConfig getCustomizeOne();
}
