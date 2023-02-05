package com.chcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chcode.ResponseResult;
import com.chcode.constants.SystemConstants;
import com.chcode.domain.entity.Menu;
import com.chcode.domain.entity.RoleMenu;
import com.chcode.domain.vo.LoadRoleMenuTreeVo;
import com.chcode.domain.vo.MenuSearchVo;
import com.chcode.domain.vo.MenuTreeVo;
import com.chcode.domain.vo.MenuVo;
import com.chcode.enums.AppHttpCodeEnum;
import com.chcode.mapper.MenuMapper;
import com.chcode.service.MenuService;
import com.chcode.service.RoleMenuService;
import com.chcode.utils.BeanCopyUtils;
import com.chcode.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-01-09 23:48:08
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public ResponseResult loadRoleMenuTree(Long id) {
        // 加载所有菜单 ==》生成菜单树List<MenuTreeVo>
        ResponseResult result = treeselect();
        List<MenuTreeVo> menuTree = (List<MenuTreeVo>)result.getData();

        // 加载当前role关联的菜单 ==》返回菜单id列表就OK
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,id);
        List<RoleMenu> list = roleMenuService.list(wrapper);
        List<Long> checkedKeys = list.stream()
                .map(roleMenu -> roleMenu.getMenuId())
                .collect(Collectors.toList());

        // 封装vo，返回
        LoadRoleMenuTreeVo loadRoleMenuTreeVo = new LoadRoleMenuTreeVo(menuTree, checkedKeys);
        return ResponseResult.okResult((loadRoleMenuTreeVo));
    }

    @Override
    public ResponseResult treeselect() {
        // 查出所有menu
        List<Menu> menus = list();
        // 将所有menu转化为，menuTreeVo
        List<MenuTreeVo> menuTreeVoList = menus.stream()
                .map(menu -> new MenuTreeVo(null, menu.getId(), menu.getMenuName(), menu.getParentId()))
                .collect(Collectors.toList());

        // 构建menuTree
        // 先设置根节点的菜单  然后去找他们的子菜单设置到children属性中
        List<MenuTreeVo> menuTree = buildMenuTree(menuTreeVoList, 0L);

        return ResponseResult.okResult(menuTree);
    }
    private List<MenuTreeVo> buildMenuTree(List<MenuTreeVo> menus,Long parentId){
        // 在流中筛选出根节点（parentId=0）
        // 为menu设置子菜单
        List<MenuTreeVo> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(selectChildren(menus, menu.getId())))
                .collect(Collectors.toList());
        return menuTree; // 返回设置好父子关系的菜单列表
    }
    // 查询parentId等于当前menuId的菜单列表
    private List<MenuTreeVo> selectChildren(List<MenuTreeVo> menus,Long menuId){
        List<MenuTreeVo> collect = menus.stream()
                .filter(menu -> menu.getParentId().equals(menuId))
                .map(menu -> menu.setChildren(selectChildren(menus, menu.getId()))) // 为当前查出的子菜单，设置对应的子菜单
                .collect(Collectors.toList());
        return collect;
    }



    @Override
    public ResponseResult deleteMenuById(Long id) {
        // 获取数据库中，所有menu的parent_id
        List<Long> parentIdList = list().stream()
                .map(menu -> menu.getParentId())
                .collect(Collectors.toList());
        if (parentIdList.contains(id)){
            return ResponseResult.errorResult(AppHttpCodeEnum.MENU_HAS_SON);
        }
        // 删除id对应的menu
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        // 注意：不能把父菜单设置为当前菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        if (menu.getParentId().equals(menu.getId())){
            return ResponseResult.errorResult(AppHttpCodeEnum.ALTER_MENU_FAIL);
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenuById(Long id) {
        Menu menu = getById(id);
        MenuSearchVo menuVo = BeanCopyUtils.copyBean(menu, MenuSearchVo.class);
        return ResponseResult.okResult(menuVo);
    }

    @Override
    public ResponseResult listMenu(String menuName, String status) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        // 根据menuName进行模糊查询
        wrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        // 根据status进行查询
        wrapper.eq(StringUtils.hasText(status),Menu::getStatus,status);
        // 按照父菜单id和orderNum进行排序
        wrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> list = list(wrapper);

        // 封装vo,返回
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(list, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        // 如果用户id为1代表管理员，menus中需要有所有菜单类型为C或者M的，状态为正常的，未被删除的权限
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if (SecurityUtils.isAdmin()){
            //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else {
            //否则  获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuByUserId(userId);
        }

        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
//        return menus;
    }

    private List<Menu> builderMenuTree(List<Menu> menus, long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取存入参数的 子Menu集合（从menus中找到）
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> menuChildren = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return menuChildren;
    }

    @Override
    public List<String> selectPermsByUserId(Long userId) {
        // 如果是管理员，返回所有的权限，menus中需要有所有菜单类型为C或者M的，状态为正常的，未被删除的权限
        if (SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }

        // 否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(userId);
    }
}


