package com.chcode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chcode.domain.entity.RoleMenu;
import com.chcode.mapper.RoleMenuMapper;
import com.chcode.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2023-01-21 17:25:59
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}


