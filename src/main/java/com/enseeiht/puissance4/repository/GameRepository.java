package com.enseeiht.puissance4.repository;

import com.enseeiht.puissance4.entity.Game;
import com.enseeiht.puissance4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByPlayer1OrPlayer2(User player1, User player2);
    List<Game> findByStatus(Game.Status status);
}
