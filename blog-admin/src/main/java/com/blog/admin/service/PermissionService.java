package com.blog.admin.service;


import com.blog.admin.model.params.PageParam;
import com.blog.admin.pojo.Permission;
import com.blog.admin.vo.Result;

public interface PermissionService {

    Result listPermission(PageParam pageParam);

    Result add(Permission permission);

    Result update(Permission permission);

    Result delete(Long id);

    Result ListUser(PageParam pageParam);
}
