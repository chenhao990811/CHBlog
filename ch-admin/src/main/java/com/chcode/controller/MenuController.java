package com.chcode.controller;

import com.chcode.ResponseResult;
import com.chcode.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 5.21.2.2 加载对应角色菜单列表树接口
     * @param id
     * @return
     */
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult loadRoleMenuTree(@PathVariable Long id){
        return menuService.loadRoleMenuTree(id);
    }

    /**
     * 5.20.2.1 获取菜单树接口
     * @return
     */
    @GetMapping("/treeselect")
    public ResponseResult treeselect(){
        return menuService.treeselect();
    }
}
