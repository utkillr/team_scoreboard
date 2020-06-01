package com.localhost.scoreboard.service;

import com.localhost.scoreboard.model.Game;
import com.localhost.scoreboard.model.Word;
import com.localhost.scoreboard.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordService {
    private WordRepository wordRepository;

    @Autowired
    public void setWordRepository(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public List<Word> findAll() {
        return wordRepository.findAll();
    }

    public Word findById(int id) {
        return wordRepository.findById(id);
    }

    public List<Word> findAllByIds(List<Integer> ids) {
        return wordRepository.findByIds(ids);
    }

    public List<Word> findNotUsedByGame(Game game) {
        return wordRepository.findNotUsedByGame(game.getId());
    }

    public List<Word> save(List<Word> words) {
        return wordRepository.saveAll(words);
    }
}
