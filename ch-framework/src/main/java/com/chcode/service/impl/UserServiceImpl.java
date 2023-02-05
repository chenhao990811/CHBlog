package com.chcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chcode.ResponseResult;
import com.chcode.domain.dto.AddUserDto;
import com.chcode.domain.dto.UpdateUserDto;
import com.chcode.domain.entity.Role;
import com.chcode.domain.entity.User;
import com.chcode.domain.entity.UserRole;
import com.chcode.domain.vo.*;
import com.chcode.enums.AppHttpCodeEnum;
import com.chcode.exception.SystemException;
import com.chcode.mapper.RoleMapper;
import com.chcode.mapper.UserMapper;
import com.chcode.mapper.UserRoleMapper;
import com.chcode.service.UserRoleService;
import com.chcode.service.UserService;
import com.chcode.utils.BeanCopyUtils;
import com.chcode.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-01-02 01:11:35
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    @Transactional
    public ResponseResult updateUser(UpdateUserDto updateUserDto) {
        // 简易实现关联表信息的更新，先删除，再添加
        User user = BeanCopyUtils.copyBean(updateUserDto, User.class);
        updateById(user);

        List<Long> roleIds = updateUserDto.getRoleIds();
        List<UserRole> updateRoleList = roleIds.stream()
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        // 删除
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,user.getId());
        userRoleMapper.delete(wrapper);
        // 添加
        userRoleService.saveBatch(updateRoleList);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserInfo(Long id) {
        // 查询 roleIds：用户所关联的角色id列表
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,id);
        List<UserRole> userRoles = userRoleMapper.selectList(wrapper);
        List<Long> roleIds = userRoles.stream()
                .map(userRole -> userRole.getRoleId())
                .collect(Collectors.toList());
        // 查询 roles：所有角色的列表
        List<Role> roleList = roleMapper.selectList(null);
        List<AdminRoleVo> roles = BeanCopyUtils.copyBeanList(roleList, AdminRoleVo.class);
        // 查询 user：用户信息
        User byId = getById(id);
        UserInfoVo2 user = BeanCopyUtils.copyBean(byId, UserInfoVo2.class);

        GetUserInfoVo userInfoVo = new GetUserInfoVo(roleIds, roles, user);

        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult addUser(AddUserDto addUserDto) {
        // dto转化为user
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
//        注意：新增用户时注意密码加密存储。
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//             *  用户名不能为空，否则提示：必需填写用户名
        if (!StringUtils.hasText(user.getUserName())){
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
//             *  用户名必须之前未存在，否则提示：用户名已存在（获取数据库中的用户名进行比较）
        List<User> userList = list();
        List<String> userNameList = userList.stream()
                .map(user1 -> user1.getUserName())
                .collect(Collectors.toList());
        if (userNameList.contains(user.getUserName())){
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
        }
//             *  手机号必须之前未存在，否则提示：手机号已存在
        List<String> phoneList = userList.stream()
                .map(user1 -> user1.getPhonenumber())
                .collect(Collectors.toList());
        if (phoneList.contains(user.getPhonenumber())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
//             *  邮箱必须之前未存在，否则提示：邮箱已存在
        List<String> emailList = userList.stream()
                .map(user1 -> user1.getEmail())
                .collect(Collectors.toList());
        if (emailList.contains(user.getEmail())){
            return ResponseResult.errorResult(AppHttpCodeEnum.EMAIL_EXIST);
        }
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult list(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        // 构建查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(userName),User::getUserName,userName);
        wrapper.eq(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber);
        wrapper.eq(StringUtils.hasText(status),User::getStatus,status);

        // 分页
        Page<User> page = new Page<>(pageNum, pageSize);
        page(page,wrapper);

        // 封装pageVo返回
        List<UserListVo> userListVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserListVo.class);
        PageVo pageVo = new PageVo(userListVos, page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult userInfo() {
        // 从token中获取userId
        Long userId = SecurityUtils.getUserId();
        // 从数据库中获取用户信息
        User user = getById(userId);
        // 封装UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if(emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);

        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail,email);
        return count(wrapper)>0;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getNickName,nickName);
        return count(wrapper)>0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,userName);
        return count(wrapper)>0;
    }
}


