package com.example.demo.service;

import com.example.demo.dtos.LoginDTO;
import com.example.demo.dtos.ResgisterUserDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepo userRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }
    public UserEntity signup(ResgisterUserDTO dto){
        UserEntity user = new UserEntity();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userRepo.save(user);
    }

    public UserEntity authenticate(LoginDTO dto){
        if(dto.getEmail()==null){
            log.info("email cant be null");
        }else
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )

        );
        return userRepo.findByEmail(dto.getEmail()).orElseThrow();
    }
    public boolean isEmailAlreadyTaken(String email){
        return userRepo.existsByEmail(email);
    }
}
