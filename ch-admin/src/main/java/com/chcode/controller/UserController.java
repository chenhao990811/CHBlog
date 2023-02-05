package com.chcode.controller;

import com.chcode.ResponseResult;
import com.chcode.domain.dto.AddUserDto;
import com.chcode.domain.dto.UpdateUserDto;
import com.chcode.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 5.26.2.2 更新用户信息接口
     * @param updateUserDto
     * @return
     */
    @PutMapping
    public ResponseResult updateUser(@RequestBody UpdateUserDto updateUserDto){
        return userService.updateUser(updateUserDto);
    }

    /**
     * 5.26.2.1 根据id查询用户信息回显接口
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getUserInfo(@PathVariable Long id){
        return userService.getUserInfo(id);
    }

    /**
     * 5.25 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable Long id){
        userService.removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 5.24.2.2 新增用户
     * 注意：新增用户时注意密码加密存储。
     *  用户名不能为空，否则提示：必需填写用户名
     *  用户名必须之前未存在，否则提示：用户名已存在
     *  手机号必须之前未存在，否则提示：手机号已存在
     *  邮箱必须之前未存在，否则提示：邮箱已存在
     * @return
     */
    @PostMapping
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        return userService.addUser(addUserDto);
    }


    /**
     * 5.23 用户列表
     * @param pageNum
     * @param pageSize
     * @param userName
     * @param phonenumber
     * @param status
     * 需要用户分页列表接口。
     *  可以根据用户名模糊搜索。
     *  可以进行手机号的搜索。
     *  可以进行状态的查询。
     * @return
     */
    @GetMapping("list")
    public ResponseResult list(Integer pageNum,Integer pageSize,String userName,String phonenumber,String status){
        return userService.list(pageNum,pageSize,userName,phonenumber,status);
    }
}
