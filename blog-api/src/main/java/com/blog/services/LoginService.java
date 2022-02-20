package com.blog.services;

import com.blog.vo.Result;
import com.blog.vo.params.LoginParams;

public interface LoginService {
    /**
     * 登录
     * @return
     */
    Result login(LoginParams loginParams);

    /**
     * 检验token
     * @param token
     * @return SysUser对象
     */
//    SysUser checkToken(String token);

    /**
     * 退出登录
     * @param token
     * @return
     */
    Result logout(String token);

    /**
     * 注册功能
     * @param loginParams
     * @return
     */
    Result register(LoginParams loginParams);
}
