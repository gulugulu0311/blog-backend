package com.blog.services;

import com.blog.pojo.SysUser;
import com.blog.vo.Result;
import com.blog.vo.UserVo;

public interface SysUserService {


    /**
     * 通过id找到作者
     *
     * @param authorId
     * @return
     */
    SysUser findUserByAuthorId(Long authorId);

    UserVo findUserVoById(Long id);

    SysUser findUser(String account, String password);

    /**
     * 通过token获取用户信息
     *
     * @param token
     * @return
     */
    Result getUserInfoByToken(String token);

    /**
     * 通过Account找到用户
     *
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);

    /**
     * 保存用户
     *
     * @param sysUser
     */
    void save(SysUser sysUser);
}
