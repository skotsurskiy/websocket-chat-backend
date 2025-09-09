package org.example.websocketchatbackend.repository;

import java.util.List;
import java.util.Optional;
import org.example.websocketchatbackend.model.Chat;
import org.example.websocketchatbackend.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

public interface ChatRepository extends JpaRepository<Chat, Long> {
  @EntityGraph(attributePaths = {"messages", "messages.user"})
  @Query("""
      SELECT c FROM Chat c
      JOIN c.users u1
      JOIN c.users u2
      WHERE u1.username = :sender AND u2.username = :receiver
      """)
  Optional<Chat> findChatByUsernames(
      @Param("sender") String sender,
      @Param("receiver") String receiver
  );

  @Query(value = """
      SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
      FROM Chat c
      JOIN c.users u1
      JOIN c.users u2
      WHERE u1.id = :senderId AND u2.id = :receiverId
      """)
  boolean existsByUsersId(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

  @EntityGraph(attributePaths = {"users"})
  List<Chat> findChatsByUsersContaining(User user);

  @EntityGraph(attributePaths = {"messages"})
  @NonNull Optional<Chat> findById(@NonNull Long id);
}
