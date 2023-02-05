package com.chcode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chcode.domain.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户和角色关联表(UserRole)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-22 03:24:04
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}

