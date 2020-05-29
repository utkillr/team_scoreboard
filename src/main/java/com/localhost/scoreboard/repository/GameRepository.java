package com.localhost.scoreboard.repository;

import com.localhost.scoreboard.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    Game findById(int id);
    List<Game> findAll();
}