package com.blog.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.mapper.CategoryMapper;
import com.blog.pojo.Category;
import com.blog.services.CategoryService;
import com.blog.vo.CategoryVo;
import com.blog.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryVo findCategoryById(Long id) {
        Category category = categoryMapper.selectById(id);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
//        categoryVo.setId(String.valueOf(id));
        categoryVo.setId(id);
        return categoryVo;
    }

    @Override
    public Result findAll() {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "category_name");
        return Result.success(copylist(categoryMapper.selectList(queryWrapper)));
    }

    @Override
    public Result findAllDetail() {
        return Result.success(copylist(categoryMapper.selectList(null)));
    }

    @Override
    public Result categoriesDetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        return Result.success(copy(category));
    }

    private List<CategoryVo> copylist(List<Category> categoryList) {
        ArrayList<CategoryVo> categoryVos = new ArrayList<>();
        for (Category category : categoryList) {
            categoryVos.add(copy(category));
        }
        return categoryVos;
    }

    private CategoryVo copy(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
//        categoryVo.setId(String.valueOf(category.getId()));
        categoryVo.setId(category.getId());
        return categoryVo;
    }
}
