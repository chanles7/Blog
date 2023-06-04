package com.java.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.common.Constants;
import com.java.common.FieldConstants;
import com.java.common.ResponseResult;
import com.java.entity.SystemConfig;
import com.java.entity.User;
import com.java.mapper.SystemConfigMapper;
import com.java.service.SystemConfigService;
import com.java.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统配置表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {


    private final UserService userService;

    /**
     * 获取系统配置
     * @return
     */
    @Override
    public ResponseResult getConfig() {
        QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
        User user = userService.getById(StpUtil.getLoginIdAsInt());
        if (user.getRoleId() > Constants.USER_ROLE_ID) queryWrapper.orderByDesc(FieldConstants.ID);
        queryWrapper.last(FieldConstants.LIMIT_ONE);
        SystemConfig systemConfig = baseMapper.selectOne(queryWrapper);
        return ResponseResult.success(systemConfig);
    }

    /**
     * 修改系统配置
     * @param systemConfig
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateConfig(SystemConfig systemConfig) {
        baseMapper.updateById(systemConfig);
        return ResponseResult.success();
    }

    //---------自定义方法----------
    @Override
    public SystemConfig getCustomizeOne() {
        return baseMapper.selectOne(new QueryWrapper<SystemConfig>().last(FieldConstants.LIMIT_ONE));
    }
}
