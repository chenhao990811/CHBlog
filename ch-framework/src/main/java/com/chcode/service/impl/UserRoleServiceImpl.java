package com.chcode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chcode.domain.entity.UserRole;
import com.chcode.mapper.UserRoleMapper;
import com.chcode.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-01-22 03:24:07
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}


