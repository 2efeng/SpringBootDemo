package com.hzf.auth.controller.system;

import com.hzf.auth.security.Claims;
import com.hzf.auth.models.system.Role;
import com.hzf.auth.models.system.User;
import com.hzf.auth.security.JWTUtil;
import com.hzf.auth.common.ResponseEntity;
import com.hzf.auth.service.system.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "auth manage")
@Slf4j
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(value = "/login", name = "login api", params = {"username", "password"})
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        User user = userService.selectByName(username);
        String pas = bCryptPasswordEncoder.encode("123456");
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.error(10001);
        }
        Claims claims = Claims.builder()
                .userId(String.valueOf(user.getId()))
                .username(user.getUsername())
                .roles(user.getRoles().stream().map(Role::getRoleName).toList())
                .build();
        String token = JWTUtil.createToken(claims);
        return ResponseEntity.ok(token);
    }


}
