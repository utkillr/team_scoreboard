package com.localhost.scoreboard.repository;

import com.localhost.scoreboard.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Player findById(int id);
    List<Player> findAll();
}