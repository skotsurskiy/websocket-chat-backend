package org.example.websocketchatbackend.mapper;

import java.util.List;
import org.example.websocketchatbackend.config.MapperConfig;
import org.example.websocketchatbackend.dto.message.MessageDto;
import org.example.websocketchatbackend.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface MessageMapper {
  @Mapping(target = "username", source = "user.username")
  @Mapping(target = "avatarUrl", source = "user.avatarUrl")
  MessageDto toMessageDto(Message message);

  @Named("toMessageDtoList")
  default List<MessageDto> toMessageDtoList(List<Message> messages) {
    return messages.stream()
        .map(this::toMessageDto)
        .toList();
  }
}
