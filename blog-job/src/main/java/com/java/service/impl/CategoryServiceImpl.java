package com.java.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.common.FieldConstants;
import com.java.common.ResponseResult;
import com.java.common.ResultCode;
import com.java.entity.Category;
import com.java.exception.BusinessException;
import com.java.mapper.CategoryMapper;
import com.java.service.CategoryService;
import com.java.util.DateUtils;
import com.java.util.PageUtils;
import com.java.util.UserHolder;
import com.java.vo.CategoryVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 博客分类表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {


    private final UserHolder userHolder;


    /**
     * 分类列表
     *
     * @param name 分类名
     * @return
     */
    @Override
    public ResponseResult listCategory(String name) {
        Page<Category> categoryPage = baseMapper.selectCategory(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), name);
        return ResponseResult.success(categoryPage);
    }

    /**
     * 分类详情
     *
     * @param id 分类id
     * @return
     */
    @Override
    public ResponseResult getCategoryById(Long id) {
        Category category = baseMapper.selectById(id);
        return ResponseResult.success(category);
    }

    /**
     * 添加分类
     *
     * @param category 分类对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertCategory(Category category) {
        Category vo = baseMapper.selectOne(new QueryWrapper<Category>().eq(FieldConstants.NAME, category.getName()));
        if (ObjectUtil.isNull(vo)) {
            throw new BusinessException("该分类名称已存在!");
        }
        int rows = baseMapper.insert(category);
        return rows > 0 ? ResponseResult.success() : ResponseResult.error("添加分类失败");
    }

    /**
     * 修改分类
     *
     * @param category 分类对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateCategory(Category category) {
        Category vo = baseMapper.selectOne(new QueryWrapper<Category>().eq(FieldConstants.NAME, category.getName()));
        Assert.isTrue(!(vo != null && !vo.getId().equals(category.getId())), ResultCode.CATEGORY_IS_EXIST.getDesc());

        int rows = baseMapper.updateById(category);

        return rows > 0 ? ResponseResult.success() : ResponseResult.error("修改分类失败");
    }

    /**
     * 删除分类
     *
     * @param id 分类id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteCategory(Long id) {
        int rows = baseMapper.deleteById(id);
        return rows > 0 ? ResponseResult.success() : ResponseResult.error("删除分类失败");
    }

    /**
     * 批量删除分类
     *
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBatch(List<Category> list) {
        List<Long> ids = list.stream().map(Category::getId).collect(Collectors.toList());
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ? ResponseResult.success() : ResponseResult.error("批量删除分类失败");
    }

    /**
     * 置顶分类
     *
     * @return 置顶分类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult top(Long id) {
        Category category = baseMapper.selectOne(new QueryWrapper<Category>().orderByDesc(FieldConstants.SORT).last(FieldConstants.LIMIT_ONE));
        Assert.isTrue(!category.getId().equals(id), ResultCode.CATEGORY_IS_TOP.getDesc());

        Category vo = Category.builder()
                .sort(category.getSort() + 1).updateTime(DateUtils.getNowDate()).id(id).build();
        int rows = baseMapper.updateById(vo);

        return rows > 0 ? ResponseResult.success() : ResponseResult.error("置顶失败");
    }

    //-----------------web端方法开始-------------

    /**
     * 首页分类列表
     *
     * @return
     */
    @Override
    public ResponseResult webList() {
        Long uid = userHolder.getCurrentUser().getId();
        List<Category> categoryList = listByUid(uid);
        List<CategoryVO> collect = categoryList.stream()
                .map(item -> BeanUtil.copyProperties(item, CategoryVO.class))
                .collect(Collectors.toList());
        return ResponseResult.success(collect);
    }


    @Override
    public List<Category> listByUid(Long uid) {
        return this.query().eq("uid", uid).list();
    }
}
