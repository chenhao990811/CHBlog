package com.chcode;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chcode.domain.entity.ArticleTag;
import com.chcode.service.ArticleTagService;
import com.chcode.service.impl.ArticleTagServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
//@Component
public class TestDemo {


    @Test
    public void testEncode(){
        // 加密后的密码（数据库中应当存储的密文）
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("1234");
        System.out.println(encode);

        System.out.println("测试合并分支");
    }
}
