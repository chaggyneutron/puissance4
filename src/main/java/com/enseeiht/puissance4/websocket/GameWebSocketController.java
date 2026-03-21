package com.enseeiht.puissance4.websocket;

import com.enseeiht.puissance4.entity.Game;
import com.enseeiht.puissance4.entity.User;
import com.enseeiht.puissance4.repository.UserRepository;
import com.enseeiht.puissance4.security.JwtUtil;
import com.enseeiht.puissance4.service.ChatService;
import com.enseeiht.puissance4.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameWebSocketController {

    private final GameService gameService;
    private final ChatService chatService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/game/{gameId}/move")
    public void playMove(@DestinationVariable Long gameId,
                         @Payload GameMessage message,
                         @Header("Authorization") String token) {
        try {
            User player = getUserFromToken(token);
            Game game = gameService.playMove(gameId, player, message.getColumn());

            GameMessage response = GameMessage.builder()
                    .type("MOVE")
                    .gameId(gameId)
                    .playerId(player.getId())
                    .username(player.getUsername())
                    .column(message.getColumn())
                    .board(game.getBoard())
                    .status(game.getStatus().name())
                    .message(game.getWinner() != null
                            ? game.getWinner().getUsername() + " a gagné !"
                            : null)
                    .build();

            // Envoie à tous les joueurs de la partie
            messagingTemplate.convertAndSend("/topic/game/" + gameId, response);

        } catch (Exception e) {
            messagingTemplate.convertAndSend("/topic/game/" + gameId,
                    GameMessage.builder().type("ERROR").message(e.getMessage()).build());
        }
    }

    @MessageMapping("/game/{gameId}/chat")
    public void sendChat(@DestinationVariable Long gameId,
                         @Payload GameMessage message,
                         @Header("Authorization") String token) {
        try {
            User sender = getUserFromToken(token);
            chatService.sendMessage(gameId, sender, message.getContent());

            GameMessage response = GameMessage.builder()
                    .type("CHAT")
                    .gameId(gameId)
                    .playerId(sender.getId())
                    .username(sender.getUsername())
                    .content(message.getContent())
                    .build();

            messagingTemplate.convertAndSend("/topic/game/" + gameId, response);

        } catch (Exception e) {
            messagingTemplate.convertAndSend("/topic/game/" + gameId,
                    GameMessage.builder().type("ERROR").message(e.getMessage()).build());
        }
    }

    @MessageMapping("/game/{gameId}/abandon")
    public void abandonGame(@DestinationVariable Long gameId,
                            @Header("Authorization") String token) {
        try {
            User player = getUserFromToken(token);
            Game game = gameService.abandonGame(gameId, player);

            GameMessage response = GameMessage.builder()
                    .type("ABANDON")
                    .gameId(gameId)
                    .playerId(player.getId())
                    .username(player.getUsername())
                    .status(game.getStatus().name())
                    .message(player.getUsername() + " a abandonné la partie.")
                    .build();

            messagingTemplate.convertAndSend("/topic/game/" + gameId, response);

        } catch (Exception e) {
            messagingTemplate.convertAndSend("/topic/game/" + gameId,
                    GameMessage.builder().type("ERROR").message(e.getMessage()).build());
        }
    }

    private User getUserFromToken(String token) {
        if (token.startsWith("Bearer ")) token = token.substring(7);
        String email = jwtUtil.extractEmail(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}
