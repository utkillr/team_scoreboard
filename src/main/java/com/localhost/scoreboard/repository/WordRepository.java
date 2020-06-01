package com.localhost.scoreboard.repository;

import com.localhost.scoreboard.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Integer> {
    Word findById(int id);
    List<Word> findAll();

    @Query(value = "SELECT * FROM Word w LEFT JOIN (SELECT * FROM word_used WHERE word_used.game_id = :game_id) wu ON w.id = wu.word_id WHERE wu.word_id IS NULL", nativeQuery = true)
    List<Word> findNotUsedByGame(@Param("game_id") Integer game);

    @Query(value = "SELECT w FROM Word w WHERE w.id IN :ids ")
    List<Word> findByIds(@Param("ids") List<Integer> ids);
}