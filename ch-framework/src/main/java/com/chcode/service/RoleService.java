package com.chcode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chcode.ResponseResult;
import com.chcode.domain.dto.AddRoleDto;
import com.chcode.domain.dto.ChangeStatusDto;
import com.chcode.domain.dto.UpdateRoleInfoDto;
import com.chcode.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-01-09 23:57:25
 */
public interface RoleService extends IService<Role> {
    List<String> selectRoleKeyByUserId(Long userId);

    ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName, String status);



    ResponseResult addRole(AddRoleDto addRoleDto);

    ResponseResult getRoleInfo(Long id);

    ResponseResult updateRoleInfo(UpdateRoleInfoDto roleInfoDto);

    ResponseResult changeStatus(ChangeStatusDto changeStatusDto);

    ResponseResult listAllRole();
}

