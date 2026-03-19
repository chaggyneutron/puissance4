package com.enseeiht.puissance4.controller;

import com.enseeiht.puissance4.entity.Ranking;
import com.enseeiht.puissance4.entity.User;
import com.enseeiht.puissance4.repository.UserRepository;
import com.enseeiht.puissance4.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rankings")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;
    private final UserRepository userRepository;

    private User getUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping
    public ResponseEntity<List<Ranking>> getLeaderboard() {
        return ResponseEntity.ok(rankingService.getLeaderboard());
    }

    @GetMapping("/me")
    public ResponseEntity<Ranking> getMyRanking(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(rankingService.getMyRanking(getUser(userDetails)));
    }
}
