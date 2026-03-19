package com.enseeiht.puissance4.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rankings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Builder.Default
    private int wins = 0;

    @Builder.Default
    private int losses = 0;

    @Builder.Default
    private int gamesPlayed = 0;

    @Builder.Default
    private double winRate = 0.0;

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
