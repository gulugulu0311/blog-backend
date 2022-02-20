package com.blog.services;

import com.blog.vo.CategoryVo;
import com.blog.vo.Result;

public interface CategoryService {

    CategoryVo findCategoryById(Long id);

    /**
     * 查询所有分类
     *
     * @return
     */
    Result findAll();

    /**
     * 详细文章分类
     *
     * @return
     */
    Result findAllDetail();

    /**
     * 根据分类查询文章
     *
     * @param id
     * @return
     */
    Result categoriesDetailById(Long id);
}