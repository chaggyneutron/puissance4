package com.enseeiht.puissance4.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "moves")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Move {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private User player;

    @Column(nullable = false)
    private int column;   // 0 à 6

    private int row;      // 0 à 5

    @Builder.Default
    private LocalDateTime playedAt = LocalDateTime.now();
}
