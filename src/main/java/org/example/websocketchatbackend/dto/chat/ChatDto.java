package org.example.websocketchatbackend.dto.chat;

import java.util.List;
import java.util.Set;
import org.example.websocketchatbackend.dto.message.MessageDto;
import org.example.websocketchatbackend.dto.user.UserResponseDto;

public record ChatDto(Long id, Set<UserResponseDto> users, List<MessageDto> messages) {
}
