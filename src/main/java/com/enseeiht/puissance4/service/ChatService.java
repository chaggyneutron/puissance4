package com.enseeiht.puissance4.service;

import com.enseeiht.puissance4.entity.ChatMessage;
import com.enseeiht.puissance4.entity.Game;
import com.enseeiht.puissance4.entity.User;
import com.enseeiht.puissance4.repository.ChatMessageRepository;
import com.enseeiht.puissance4.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final GameRepository gameRepository;

    public ChatMessage sendMessage(Long gameId, User sender, String content) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Partie non trouvée"));
        ChatMessage message = ChatMessage.builder()
                .game(game).sender(sender).content(content).build();
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getMessages(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Partie non trouvée"));
        return chatMessageRepository.findByGameOrderBySentAtAsc(game);
    }
}
