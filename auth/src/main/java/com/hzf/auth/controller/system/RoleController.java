package com.hzf.auth.controller.system;

import com.hzf.auth.models.system.Role;
import com.hzf.auth.service.system.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@Tag(name = "role manage")
@Slf4j
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping(value = "/create", name = "create role")
    public ResponseEntity<String> createUser() {
        log.info("create role");
        return ResponseEntity.ok("successfully");
    }


    @GetMapping(value = "/list", name = "get all role")
    public ResponseEntity<List<Role>> getRoles() {
        log.info("get roles");
        List<Role> users = roleService.getRoles();
        return ResponseEntity.ok(users);
    }

}
