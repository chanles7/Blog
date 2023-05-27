package com.java.kael.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.kael.common.FieldConstants;
import com.java.kael.common.ResponseResult;
import com.java.kael.entity.WebConfig;
import com.java.kael.mapper.WebConfigMapper;
import com.java.kael.service.WebConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 网站配置表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-11-27
 */
@Service
public class WebConfigServiceImpl extends ServiceImpl<WebConfigMapper, WebConfig> implements WebConfigService {

    /**
     * 网站配置
     * @return
     */
    @Override
    public ResponseResult listWebConfig() {
        WebConfig webConfig = baseMapper.selectOne(new QueryWrapper<WebConfig>()
                .last(FieldConstants.LIMIT_ONE));
        return ResponseResult.success(webConfig);
    }

    /**
     * 修改网站配置
     * @param webConfig
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateWebConfig(WebConfig webConfig) {
        baseMapper.updateById(webConfig);
        return ResponseResult.success();
    }
}
