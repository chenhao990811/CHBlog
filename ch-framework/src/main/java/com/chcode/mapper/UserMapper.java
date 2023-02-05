package com.chcode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chcode.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-02 01:11:30
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}

