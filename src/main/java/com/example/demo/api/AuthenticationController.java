package com.example.demo.api;

import com.example.demo.dtos.LoginDTO;
import com.example.demo.dtos.ResgisterUserDTO;
import com.example.demo.entity.LoginResponse;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService service;

    public AuthenticationController(JwtService jwtService, AuthenticationService service) {
        this.jwtService = jwtService;
        this.service = service;
    }
    @PostMapping("/register")
    public ResponseEntity<UserEntity> register(@RequestBody ResgisterUserDTO dto){
        UserEntity registerdUser = service.signup(dto);
        return ResponseEntity.ok(registerdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginDTO dto){
        UserEntity authenticateUser = service.authenticate(dto);
        String token = jwtService.generateToken(authenticateUser);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/")
    public String test(){
        return "Working";
    }
}
