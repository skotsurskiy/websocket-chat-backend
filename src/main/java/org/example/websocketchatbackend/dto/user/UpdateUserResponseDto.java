package org.example.websocketchatbackend.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserResponseDto {
    String token;
    String username;
    String avatarUrl;
}
