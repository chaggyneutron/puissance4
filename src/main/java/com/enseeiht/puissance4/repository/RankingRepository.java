package com.enseeiht.puissance4.repository;

import com.enseeiht.puissance4.entity.Ranking;
import com.enseeiht.puissance4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
    Optional<Ranking> findByUser(User user);
    List<Ranking> findAllByOrderByWinsDesc();
}
