package com.chcode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chcode.ResponseResult;
import com.chcode.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-01-09 23:48:07
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long userId);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult listMenu(String menuName, String status);

    ResponseResult getMenuById(Long id);

    ResponseResult updateMenu(Menu menu);

    ResponseResult deleteMenuById(Long id);

    ResponseResult treeselect();

    ResponseResult loadRoleMenuTree(Long id);
}

