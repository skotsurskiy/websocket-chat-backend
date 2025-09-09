package org.example.websocketchatbackend.service;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.websocketchatbackend.dto.chat.ChatDto;
import org.example.websocketchatbackend.dto.message.MessageRequestDto;
import org.example.websocketchatbackend.exception.ChatAlreadyExistsException;
import org.example.websocketchatbackend.exception.ChatNotFoundException;
import org.example.websocketchatbackend.exception.MessageBlankContentException;
import org.example.websocketchatbackend.exception.UserNotFoundException;
import org.example.websocketchatbackend.mapper.ChatMapper;
import org.example.websocketchatbackend.model.Chat;
import org.example.websocketchatbackend.model.Message;
import org.example.websocketchatbackend.model.User;
import org.example.websocketchatbackend.repository.ChatRepository;
import org.example.websocketchatbackend.repository.MessageRepository;
import org.example.websocketchatbackend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
  private final ChatRepository chatRepository;
  private final UserRepository userRepository;
  private final ChatMapper chatMapper;
  private final MessageRepository messageRepository;

  @Override
  public ChatDto createChat(String username) {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User receiver = userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(username));

    if (chatRepository.existsByUsersId(user.getId(), receiver.getId())) {
      throw new ChatAlreadyExistsException("Chat with users: %s, %s already exists");
    }

    Chat chat = new Chat();
    chat.setUsers(Set.of(user, receiver));
    chat.setMessages(List.of());

    return chatMapper.toChatDto(chatRepository.save(chat));
  }

  @Override
  public List<ChatDto> findAllChats() {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return chatRepository.findChatsByUsersContaining(user).stream()
        .peek(this::assignLastMessageToChat)
        .map(chatMapper::toChatDto)
        .toList();
  }

  @Override
  public ChatDto findChatByUsername(String receiver) {
    User sender = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Chat chat = chatRepository.findChatByUsernames(sender.getUsername(), receiver)
        .orElseThrow(() -> new ChatNotFoundException("Chat for users: '%s', '%s' not found"
            .formatted(sender.getUsername(), receiver)));
    chat.setUsers(Set.of());

    return chatMapper.toChatDto(chat);
  }

  @Override
  public void sendMessage(MessageRequestDto requestDto) {
    User sender = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (requestDto.content().isBlank()) {
      throw new MessageBlankContentException();
    }

    Chat chat = chatRepository.findById(requestDto.chatId())
        .orElseThrow(() -> new ChatNotFoundException("Chat by id: '%s' not found"));

    Message message = new Message();
    message.setUser(sender);
    message.setContent(requestDto.content());
    message.setChat(chat);

    chat.getMessages().add(message);
    chatRepository.save(chat);
  }

  private void assignLastMessageToChat(Chat chat) {
    Message lastMessage = messageRepository.findTopByChatOrderBySentAtDesc(chat);
    chat.setMessages(lastMessage == null ? List.of() : List.of(lastMessage));
  }
}
