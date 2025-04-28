package com.example.demo.dtos;

import lombok.Data;

@Data
public class ResgisterUserDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
