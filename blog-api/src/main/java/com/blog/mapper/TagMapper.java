package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.pojo.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> findArticleById(Long id);

    List<Long> findHotsTagIds(int limit);

    List<Tag> findTagsByTageIds(List<Long> tagIds);
}
