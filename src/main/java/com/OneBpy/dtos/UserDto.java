package com.OneBpy.dtos;

import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String role;
}
