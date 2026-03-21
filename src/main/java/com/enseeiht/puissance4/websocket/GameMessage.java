package com.enseeiht.puissance4.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameMessage {

    private String type;      // MOVE, CHAT, JOIN, ABANDON, ERROR
    private Long gameId;
    private Long playerId;
    private String username;
    private Integer column;   // pour un coup
    private String board;     // état du plateau après le coup
    private String status;    // état de la partie
    private String content;   // pour le chat
    private String message;   // message d'info/erreur
}
