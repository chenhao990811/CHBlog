package com.chcode.controller;

import com.chcode.ResponseResult;
import com.chcode.annotation.SystemLog;
import com.chcode.domain.entity.User;
import com.chcode.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(tags = "06 用户",description = "用户相关接口")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 3.13 个人信息查询接口
     * @return
     */
    @GetMapping("/userInfo")
    @ApiOperation(value = "个人信息查询",notes = "查询用户的个人信息，从token中获取userId")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    /**
     * 3.15 更新个人信息接口
     * @param user
     * @return
     */
    @PutMapping("userInfo")
    @SystemLog(businessName = "更新个人信息接口")
    @ApiOperation(value = "更新个人信息",notes = "更新个人信息")
    @ApiImplicitParam(name = "user",value = "用户实体")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    /**
     * 3.16 用户注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册",notes = "填写用户信息，进行注册")
    @ApiImplicitParam(name = "user",value = "用户实体")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }

}
