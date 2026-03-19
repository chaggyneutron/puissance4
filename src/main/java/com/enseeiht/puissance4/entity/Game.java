package com.enseeiht.puissance4.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "games")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player1_id", nullable = false)
    private User player1;

    @ManyToOne
    @JoinColumn(name = "player2_id")
    private User player2;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private User winner;

    @ManyToOne
    @JoinColumn(name = "current_turn_id")
    private User currentTurn;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.WAITING;

    // board 6x7 sérialisé en string : "0000000,0000000,0000000,0000000,0000000,0000000"
    // 0 = vide, 1 = player1, 2 = player2
    @Builder.Default
    private String board = "0000000,0000000,0000000,0000000,0000000,0000000";

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime finishedAt;

    public enum Status {
        WAITING, IN_PROGRESS, FINISHED, ABANDONED
    }
}
