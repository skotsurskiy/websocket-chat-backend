package org.example.websocketchatbackend.repository;

import org.example.websocketchatbackend.model.Chat;
import org.example.websocketchatbackend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
  Message findTopByChatOrderBySentAtDesc(Chat chat);
}
