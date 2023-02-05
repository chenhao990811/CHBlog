package com.chcode.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserInfoVo {
    private List<Long> roleIds;
    private List<AdminRoleVo> roles;
    private UserInfoVo2 user;
}
