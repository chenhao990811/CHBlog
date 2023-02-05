package com.chcode.controller;

import com.chcode.ResponseResult;
import com.chcode.domain.dto.AddRoleDto;
import com.chcode.domain.dto.ChangeStatusDto;
import com.chcode.domain.dto.UpdateRoleInfoDto;
import com.chcode.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;


    /**
     * 5.24.2.1 查询角色列表接口
     * 注意：查询的是所有状态正常的角色
     * @return
     */
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }

    /**
     * 5.22 删除角色
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteRoleById(@PathVariable Long id){
        roleService.removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 5.21.2.3 更新角色信息接口
     * @param roleInfoDto
     * @return
     */
    @PutMapping
    public ResponseResult updateRoleInfo(@RequestBody UpdateRoleInfoDto roleInfoDto){
        return roleService.updateRoleInfo(roleInfoDto);
    }

    /**
     * 5.21.2.1 角色信息回显接口
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getRoleInfo(@PathVariable Long id){
        return roleService.getRoleInfo(id);
    }

    /**
     * 5.20.2.2 新增角色接口
     * 注意：需要在sys_role_menu表中，增加角色与菜单的对应关系
     *      自动填充需要在实体类怎加相应的注解
     * @param addRoleDto
     * @return
     */
    @PostMapping
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }


    /**
     * 5.19 改变角色状态
     * @param changeStatusDto
     * @return
     */
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeStatusDto changeStatusDto){
        return roleService.changeStatus(changeStatusDto);
    }

    /**
     * 5.18 角色列表
     * 需要有角色列表分页查询的功能。
     * 要求能够针对角色名称进行模糊查询。
     * 要求能够针对状态进行查询。
     * 要求按照role_sort进行升序排列。
     * @param pageNum
     * @param pageSize
     * @param roleName
     * @param status
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getRoleList(Integer pageNum,Integer pageSize,String roleName,String status){
        return roleService.getRoleList(pageNum,pageSize,roleName,status);
    }
}
