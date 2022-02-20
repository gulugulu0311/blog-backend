package com.blog.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.admin.pojo.SysUser;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<SysUser> {
}
