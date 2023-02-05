package com.chcode.utils;

import com.chcode.domain.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils
{

    public static Long getUserId() {
        return getLoginUser().getUser().getId();
    }
    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser()
    {
        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        // TODO 修复空指针
        SecurityContext context = SecurityContextHolder.getContext();

        return SecurityContextHolder.getContext().getAuthentication();
    }



    public static Boolean isAdmin(){
        // TODO 逻辑有误，用户是否为管理员应当与用户的角色相关，而不是用户id
        Long id = getLoginUser().getUser().getId();
        return id != null && id.equals(1L);
    }
}