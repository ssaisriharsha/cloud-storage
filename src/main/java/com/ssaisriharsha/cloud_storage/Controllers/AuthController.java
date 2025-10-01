package com.ssaisriharsha.cloud_storage.Controllers;

import com.ssaisriharsha.cloud_storage.DTOs.UserLoginDTO;
import com.ssaisriharsha.cloud_storage.Services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthService service;
    public AuthController(AuthService service) {
        this.service =service;
    }
    @PostMapping("/login")
    public ResponseEntity<Map<Object, Object>> login(@RequestBody @Valid UserLoginDTO loginDTO) {
        String jwt= service.login(loginDTO);
        Map<Object, Object> map=new HashMap<>();
        map.put("Status", 200);
        map.put("Message", "Login success. Use the below JWT for further requests");
        map.put("JWT", jwt);
        return ResponseEntity.ok(map);
    }
    public ResponseEntity<Map<Object, Object>> signup(@RequestBody @Valid UserLoginDTO loginDTO) {
        service.signup(loginDTO);
        return ResponseEntity.ok(null);
    }
}
