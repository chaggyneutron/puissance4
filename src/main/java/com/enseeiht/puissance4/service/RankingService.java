package com.enseeiht.puissance4.service;

import com.enseeiht.puissance4.entity.Ranking;
import com.enseeiht.puissance4.entity.User;
import com.enseeiht.puissance4.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;

    @Transactional
    public void updateRanking(User winner, User loser) {
        Ranking winnerRanking = rankingRepository.findByUser(winner)
                .orElseThrow(() -> new RuntimeException("Ranking non trouvé"));
        Ranking loserRanking = rankingRepository.findByUser(loser)
                .orElseThrow(() -> new RuntimeException("Ranking non trouvé"));

        winnerRanking.setWins(winnerRanking.getWins() + 1);
        winnerRanking.setGamesPlayed(winnerRanking.getGamesPlayed() + 1);
        winnerRanking.setWinRate((double) winnerRanking.getWins()
                / winnerRanking.getGamesPlayed() * 100);
        winnerRanking.setUpdatedAt(LocalDateTime.now());

        loserRanking.setLosses(loserRanking.getLosses() + 1);
        loserRanking.setGamesPlayed(loserRanking.getGamesPlayed() + 1);
        loserRanking.setWinRate((double) loserRanking.getWins()
                / loserRanking.getGamesPlayed() * 100);
        loserRanking.setUpdatedAt(LocalDateTime.now());

        rankingRepository.save(winnerRanking);
        rankingRepository.save(loserRanking);
    }

    public List<Ranking> getLeaderboard() {
        return rankingRepository.findAllByOrderByWinsDesc();
    }

    public Ranking getMyRanking(User user) {
        return rankingRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Ranking non trouvé"));
    }
}
