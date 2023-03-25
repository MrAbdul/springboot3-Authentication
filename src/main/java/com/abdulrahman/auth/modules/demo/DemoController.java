package com.abdulrahman.auth.modules.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/demo-controller")
public class DemoController {


    @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello from secured endpoint for all or no roles");
    }
    @GetMapping("/roleUser")
    public ResponseEntity<String> sayHelloUser(){
        return ResponseEntity.ok("Hello from secured endpoint for Users only");
    }
    @GetMapping("/roleAdmin")
    public ResponseEntity<String> sayHelloAdmin(){
        return ResponseEntity.ok("Hello from secured endpoint for Admins only");
    }
    @GetMapping("/roleAdminOrUser")
    public ResponseEntity<String> sayHelloAdminOrUser(){
        return ResponseEntity.ok("Hello from secured endpoint for Admins or User");
    }


}