package com.example.demo.api;

import com.example.demo.dtos.LoginDTO;
import com.example.demo.dtos.ResgisterUserDTO;
import com.example.demo.entity.LoginResponse;
import com.example.demo.entity.UserEntity;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserRepo repo;
    private final JwtService jwtService;
    private final AuthenticationService service;

    public AuthenticationController(JwtService jwtService, AuthenticationService service,UserRepo repo) {
        this.jwtService = jwtService;
        this.service = service;
        this.repo = repo;
    }
    @PostMapping("/register")
    public ResponseEntity<UserEntity> register(@RequestBody ResgisterUserDTO dto){
            if(service.isEmailAlreadyTaken(dto.getEmail())){
                return new ResponseEntity<UserEntity>(HttpStatus.BAD_REQUEST);
            }
            try {
                UserEntity registerdUser = service.signup(dto);
                return new ResponseEntity<>(registerdUser,HttpStatus.CREATED);
            }catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginDTO dto){
        log.info("email={}", dto.getEmail());
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
