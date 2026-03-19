package com.enseeiht.puissance4.repository;

import com.enseeiht.puissance4.entity.ChatMessage;
import com.enseeiht.puissance4.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByGameOrderBySentAtAsc(Game game);
}
