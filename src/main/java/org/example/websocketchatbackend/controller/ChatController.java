package org.example.websocketchatbackend.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.websocketchatbackend.dto.chat.ChatDto;
import org.example.websocketchatbackend.dto.message.MessageRequestDto;
import org.example.websocketchatbackend.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {
  private final ChatService chatService;

  @PostMapping("/create/{username}")
  @ResponseStatus(HttpStatus.CREATED)
  public ChatDto createChat(@PathVariable String username) {
    return chatService.createChat(username);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<ChatDto> findAllChats() {
    return chatService.findAllChats();
  }

  @GetMapping("/{username}")
  @ResponseStatus(HttpStatus.OK)
  public ChatDto findChatByUsername(@PathVariable String username) {
    return chatService.findChatByUsername(username);
  }

  @PostMapping("/send")
  @ResponseStatus(HttpStatus.CREATED)
  public void sendMessage(@RequestBody MessageRequestDto requestDto) {
    chatService.sendMessage(requestDto);
  }
}
