package org.example.websocketchatbackend.service;

import java.util.List;
import org.example.websocketchatbackend.dto.chat.ChatDto;
import org.example.websocketchatbackend.dto.message.MessageRequestDto;

public interface ChatService {
  ChatDto createChat(String username);

  List<ChatDto> findAllChats();

  ChatDto findChatByUsername(String username);

  void sendMessage(MessageRequestDto requestDto);
}
