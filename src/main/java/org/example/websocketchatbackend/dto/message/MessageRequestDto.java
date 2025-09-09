package org.example.websocketchatbackend.dto.message;

public record MessageRequestDto(Long chatId, String content) {
}
