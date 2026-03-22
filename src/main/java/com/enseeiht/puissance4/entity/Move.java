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

    @Column(name = "col_index")
    private int column;

    @Column(name = "row_index")
    private int row;

    @Builder.Default
    private LocalDateTime playedAt = LocalDateTime.now();
}
