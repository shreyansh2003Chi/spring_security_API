package com.example.SpringSecurity.dtos;


import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String username;
    private String role;
}