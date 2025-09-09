package org.example.websocketchatbackend.mapper;

import org.example.websocketchatbackend.config.MapperConfig;
import org.example.websocketchatbackend.dto.chat.ChatDto;
import org.example.websocketchatbackend.model.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = MessageMapper.class)
public interface ChatMapper {
  @Mapping(target = "messages", qualifiedByName = "toMessageDtoList")
  ChatDto toChatDto(Chat chat);
}
