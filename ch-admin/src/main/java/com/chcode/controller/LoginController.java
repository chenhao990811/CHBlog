package com.chcode.controller;

import com.chcode.ResponseResult;
import com.chcode.config.SecurityConfig;
import com.chcode.domain.entity.LoginUser;
import com.chcode.domain.entity.Menu;
import com.chcode.domain.entity.User;
import com.chcode.domain.vo.AdminUserInfoVo;
import com.chcode.domain.vo.RoutersVo;
import com.chcode.domain.vo.UserInfoVo;
import com.chcode.enums.AppHttpCodeEnum;
import com.chcode.exception.SystemException;
import com.chcode.service.LoginService;
import com.chcode.service.MenuService;
import com.chcode.service.RoleService;
import com.chcode.utils.BeanCopyUtils;
import com.chcode.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    /**
     * 5.3 退出登录接口
     * @return
     */
    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }

    /**
     * 5.2 获取权限信息，以tree的形式返回
     * @return
     */
    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        // 查询menu 结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        // 封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    /**
     * 5.2 获取用户信息，包括权限和角色信息
     * @return
     */
    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long userId = SecurityUtils.getUserId();

        //根据用户id查询权限信息（查出menu表中的perms字段）
        List<String> perms = menuService.selectPermsByUserId(userId);

        //根据用户id查询角色信息（查出role表中的role_key字段）
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(userId);
//        List<String> roleKeyList = null;

        //获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        //封装数据返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeyList, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    /**
     * 5.1 后台登录
     * @param user
     * @return
     */
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    /**
     * 5.2 获取权限菜单
     * @return
     */
    @GetMapping("/getMenu")
    public ResponseResult getMenu(){
        Long userId = SecurityUtils.getUserId();
        List<String> menuList = menuService.selectPermsByUserId(userId);
        return ResponseResult.okResult(menuList);
    }
}
