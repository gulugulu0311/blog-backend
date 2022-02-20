package com.blog.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.admin.mapper.AdminMapper;
import com.blog.admin.mapper.PermissionMapper;
import com.blog.admin.pojo.Admin;
import com.blog.admin.pojo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    public Admin findAdminByUserName(String username) {
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .last("limit 1");
        Admin adminUser = adminMapper.selectOne(wrapper);
        return adminUser;
    }

    public List<Permission> findPermissionsByAdminId(Long adminId) {
        return permissionMapper.findPermissionsByAdminId(adminId);
    }

}
