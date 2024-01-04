package com.hzf.auth.controller.system;

import com.hzf.auth.models.system.User;
import com.hzf.auth.service.system.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(name = "user manage")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/create", name = "create role")
    public ResponseEntity<String> createUser() {
        log.info("create user");
        return ResponseEntity.ok("successfully");
    }


    @GetMapping(value = "/list", name = "get all user")
    public ResponseEntity<List<User>> getUsers() {
        log.info("get users");
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

}
