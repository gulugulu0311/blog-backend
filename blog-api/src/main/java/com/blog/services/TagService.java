package com.blog.services;

import com.blog.vo.Result;
import com.blog.vo.TagVo;

import java.util.List;

public interface TagService {
    /**
     * 根据文章id 查询标签列表
     *
     * @param id
     * @return
     */
    List<TagVo> findTagByArticleId(Long id);

    /**
     * 查询最热的标签 前 limit 条
     *
     * @param limit
     * @return
     */
    Result hots(int limit);

    /**
     * 查询所有标签
     *
     * @return
     */
    Result findAll();

    /**
     * 查询所有标签 详细
     *
     * @return
     */
    Result findAllDetail();

    /**
     * 根据标签 文章分类
     *
     * @param id
     * @return
     */
    Result findDetailById(Long id);
}
