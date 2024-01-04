package com.hzf.auth.controller.business;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/my")
@Tag(name = "my api")
public class MyController {

    @GetMapping(value = {"/hello"}, name = "hello api")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello world");
    }

}
