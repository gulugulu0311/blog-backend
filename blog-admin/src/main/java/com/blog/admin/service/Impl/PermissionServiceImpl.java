package com.blog.admin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.admin.mapper.PermissionMapper;
import com.blog.admin.mapper.UserMapper;
import com.blog.admin.model.params.PageParam;
import com.blog.admin.pojo.Permission;
import com.blog.admin.pojo.SysUser;
import com.blog.admin.service.PermissionService;
import com.blog.admin.vo.PageResult;
import com.blog.admin.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result listPermission(PageParam pageParam) {
        Page<Permission> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(pageParam.getQueryString())) {
            queryWrapper.eq("name", pageParam.getQueryString());
        }
        Page<Permission> permissionPage = permissionMapper.selectPage(page, queryWrapper);
        PageResult<Permission> pageResult = new PageResult<>();
        pageResult.setList(permissionPage.getRecords());
        pageResult.setTotal(permissionPage.getTotal());
        return Result.success(pageResult);
    }

    @Override
    public Result add(Permission permission) {
        this.permissionMapper.insert(permission);
        return Result.success(null);
    }

    @Override
    public Result update(Permission permission) {
        this.permissionMapper.updateById(permission);
        return Result.success(null);
    }

    @Override
    public Result delete(Long id) {
        this.permissionMapper.deleteById(id);
        return Result.success(null);
    }

    @Override
    public Result ListUser(PageParam pageParam) {
        Page<SysUser> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        log.info(pageParam.getQueryString());
        if (StringUtils.isNotBlank(pageParam.getQueryString())) {
            queryWrapper.eq("nickname", pageParam.getQueryString());

        }
//        Page<SysUser> permissionPage = us.selectPage(page, queryWrapper);
        Page<SysUser> sysUserPage = userMapper.selectPage(page, queryWrapper);
        PageResult<SysUser> pageResult = new PageResult<>();
        pageResult.setList(sysUserPage.getRecords());
        pageResult.setTotal(sysUserPage.getTotal());
        return Result.success(pageResult);
    }
}
