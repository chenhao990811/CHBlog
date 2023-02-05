package com.chcode;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordEncoderTest {
    @Test
    public void testEncode(){
        // 加密后的密码（数据库中应当存储的密文）
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        String encode = bCryptPasswordEncoder.encode("1234"); // $2a$10$8OZY/5uYnwstOiigWq4P3ep0DWvUXBg4q5bqU.tFpror1bmHL5Oq.
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode1 = passwordEncoder.encode("1234");
        System.out.println(encode1);
        System.out.println("===================================================");
//        System.out.println(encode);
        System.out.println(passwordEncoder.matches(encode1, "1234"));
    }
}
