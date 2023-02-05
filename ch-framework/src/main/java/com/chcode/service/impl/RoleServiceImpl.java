package com.chcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chcode.ResponseResult;
import com.chcode.domain.dto.AddRoleDto;
import com.chcode.domain.dto.ChangeStatusDto;
import com.chcode.domain.dto.UpdateRoleInfoDto;
import com.chcode.domain.entity.Menu;
import com.chcode.domain.entity.Role;
import com.chcode.domain.entity.RoleMenu;
import com.chcode.domain.vo.*;
import com.chcode.mapper.MenuMapper;
import com.chcode.mapper.RoleMapper;
import com.chcode.mapper.RoleMenuMapper;
import com.chcode.service.MenuService;
import com.chcode.service.RoleMenuService;
import com.chcode.service.RoleService;
import com.chcode.utils.BeanCopyUtils;
import com.chcode.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-01-09 23:57:25
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public ResponseResult updateRoleInfo(UpdateRoleInfoDto roleInfoDto) {
        // 更新ch_role角色表信息
        Role role = BeanCopyUtils.copyBean(roleInfoDto, Role.class);
        updateById(role);
        Long id = roleInfoDto.getId();
        // 更新ch_role_menu角色菜单关联表信息
        // 00 获取待更新的关联表数据
        List<Long> menuIdsFromPage = roleInfoDto.getMenuIds();
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, id);
        List<Long> menuIdsFromDatabase = roleMenuService.list(wrapper).stream()
                .map(roleMenu -> roleMenu.getMenuId())
                .collect(Collectors.toList());

        // 01 判断menu菜单需要进行的操作
        if (!ObjectUtils.isEmpty(menuIdsFromPage) && ObjectUtils.isEmpty(menuIdsFromDatabase)){
            // 数据库中，新增关系
            insertRoleMenu(id,menuIdsFromPage);
        }else if (ObjectUtils.isEmpty(menuIdsFromPage) && !ObjectUtils.isEmpty(menuIdsFromDatabase)){
            // 数据库中，删除关系
            deleteRoleMenu(id);
        }else if (ObjectUtils.isEmpty(menuIdsFromPage) && ObjectUtils.isEmpty(menuIdsFromDatabase)){
            // 无变化，直接返回
            return ResponseResult.okResult();
        }
        if (isTagsChange(menuIdsFromPage,menuIdsFromDatabase)){
            // 删除数据库中对应的记录
            deleteRoleMenu(id);
            // 新增
            insertRoleMenu(id,menuIdsFromPage);
        }

        return ResponseResult.okResult();
    }

    /**
     * 功能：判断两个集合是否一致
     *  使用list自带的sort方法先进性排序，然后转成toString去判断两个集合是否相等
     * @param menuIdsFromPage
     * @param menuIdsFromDatabase
     * @return true：集合不一致；false：集合一致
     */
    private boolean isTagsChange(List<Long> menuIdsFromPage, List<Long> menuIdsFromDatabase) {
        List<String> tagsStringList = changeToStringList(menuIdsFromPage);
        List<String> tags2StringList = changeToStringList(menuIdsFromDatabase);

        tagsStringList.sort(Comparator.comparing(String::hashCode));
        tags2StringList.sort(Comparator.comparing(String::hashCode));

        return !tagsStringList.toString().equals(tags2StringList.toString());
    }

    private List<String> changeToStringList(List<Long> menuIdsFromPage) {
        List<String> collect = menuIdsFromPage.stream()
                .map((Function<Long, String>) aLong -> aLong.toString())
                .collect(Collectors.toList());
        return collect;
    }

    // 根据role_id，删除sys_role_menu表中对应数据
    private void deleteRoleMenu(Long id) {
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,id);
        roleMenuService.remove(wrapper);
    }

    // 根据roleId，menuIds构建RoleMenu，进行sys_role_menu表数据的插入
    private void insertRoleMenu(Long id, List<Long> menuIdsFromPage) {
        List<RoleMenu> roleMenus = menuIdsFromPage.stream()
                .map(menuId -> new RoleMenu(id, menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
    }

    @Override
    public ResponseResult getRoleInfo(Long id) {
        Role role = getById(id);
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    public ResponseResult addRole(AddRoleDto addRoleDto) {
        // addRoleDto转化为Role，数据库中添加role
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        save(role);

        // 获取menuIds，在sys_role_menu表中，增加角色与菜单的对应关系
        // 01 将menuIds 转化为 List<RoleMenu>
        List<Long> menuIds = addRoleDto.getMenuIds();
        if (ObjectUtils.isEmpty(menuIds)){
            return ResponseResult.okResult();
        }
        Long roleId = role.getId();
        List<RoleMenu> roleMenus = menuIds.stream()
                .map(menuid -> new RoleMenu(roleId, menuid))
                .collect(Collectors.toList());
        // 02 将roleMenus存入sys_role_menu表中
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }


    @Override
    public ResponseResult changeStatus(ChangeStatusDto changeStatusDto) {
        LambdaUpdateWrapper<Role> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Role::getStatus,changeStatusDto.getStatus());
        wrapper.eq(Role::getId,changeStatusDto.getRoleId());
        update(wrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus,"0");
        List<Role> roleList = list(wrapper);

        List<ListAllRoleVo> listAllRoleVos = BeanCopyUtils.copyBeanList(roleList, ListAllRoleVo.class);
        return ResponseResult.okResult(listAllRoleVos);
    }

    @Override
    public ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName, String status) {
        // 构建查询条件
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName)
                .eq(StringUtils.hasText(status),Role::getStatus,status);
        wrapper.orderByAsc(Role::getRoleSort);
        // 分页
        Page<Role> page = new Page<>(pageNum, pageSize);
        page(page,wrapper);

        // 封装pageVo返回
        List<Role> records = page.getRecords();
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(records, RoleVo.class);
        PageVo pageVo = new PageVo(roleVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public List<String> selectRoleKeyByUserId(Long userId) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if (SecurityUtils.isAdmin()){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }

        //否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(userId);
    }
}


