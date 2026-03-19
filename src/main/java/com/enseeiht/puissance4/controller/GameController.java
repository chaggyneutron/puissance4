package com.enseeiht.puissance4.controller;

import com.enseeiht.puissance4.entity.Game;
import com.enseeiht.puissance4.entity.User;
import com.enseeiht.puissance4.repository.UserRepository;
import com.enseeiht.puissance4.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final UserRepository userRepository;

    private User getUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping
    public ResponseEntity<Game> createGame(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(gameService.createGame(getUser(userDetails)));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Game> joinGame(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(gameService.joinGame(id, getUser(userDetails)));
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<Game> playMove(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                gameService.playMove(id, getUser(userDetails), body.get("column")));
    }

    @PostMapping("/{id}/abandon")
    public ResponseEntity<Game> abandonGame(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(gameService.abandonGame(id, getUser(userDetails)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGame(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getGame(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Game>> getAvailableGames() {
        return ResponseEntity.ok(gameService.getAvailableGames());
    }
}
