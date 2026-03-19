package com.enseeiht.puissance4.repository;

import com.enseeiht.puissance4.entity.Move;
import com.enseeiht.puissance4.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MoveRepository extends JpaRepository<Move, Long> {
    List<Move> findByGameOrderByPlayedAtAsc(Game game);
}
