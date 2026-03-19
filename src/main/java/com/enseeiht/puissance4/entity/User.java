package com.enseeiht.puissance4.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    private int wins = 0;

    @Builder.Default
    private int losses = 0;

    @Builder.Default
    private boolean isOnline = false;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.PLAYER;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    
    @OneToMany(mappedBy = "player1", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Game> gamesAsPlayer1 = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "player2", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Game> gamesAsPlayer2 = new java.util.ArrayList<>();

    public enum Role {
        PLAYER, ADMIN
    }
}
