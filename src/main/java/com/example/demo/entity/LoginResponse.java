package com.example.demo.entity;

import lombok.Data;

@Data
public class LoginResponse {
    private long expiresIn;
    private String token;

}
