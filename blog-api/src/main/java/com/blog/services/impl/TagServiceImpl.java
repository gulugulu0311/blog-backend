package com.blog.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.mapper.TagMapper;
import com.blog.pojo.Tag;
import com.blog.services.TagService;
import com.blog.vo.Result;
import com.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagVo> findTagByArticleId(Long id) {
        // MyBatisPlus不支持多表查询
        List<Tag> tags = tagMapper.findArticleById(id);
        return copyList(tags);
    }

    @Override
    public Result hots(int limit) {
        /**
         * 标签所拥有的文章数量最多 最热标签
         * 查询 根据tag_id分组 计数，从小到大 排列 取前 limit 个
         */
        List<Long> tagIds = tagMapper.findHotsTagIds(limit);
        if (CollectionUtils.isEmpty(tagIds)) {
            return Result.success(Collections.emptyList());
        }
        /**
         * 需求的是 tagId 和 tagName Tag对象
         */
        List<Tag> tagList = tagMapper.findTagsByTageIds(tagIds);
        return Result.success(tagList);
    }

    @Override
    public Result findAll() {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "tag_name");
        List<Tag> tags = tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tags));
    }

    @Override
    public Result findAllDetail() {
        return Result.success(copyList(tagMapper.selectList(null)));
    }

    @Override
    public Result findDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        return Result.success(copy(tag));
    }

    private List<TagVo> copyList(List<Tag> tags) {
        List<TagVo> tagVos = new ArrayList<>();
        for (Tag tag : tags) {
            tagVos.add(copy(tag));
        }
        return tagVos;
    }

    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag, tagVo);
//        tagVo.setId(String.valueOf(tag.getId()));
        tagVo.setId(tag.getId());
        return tagVo;
    }
}
