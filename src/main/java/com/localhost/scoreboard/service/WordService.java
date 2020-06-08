package com.localhost.scoreboard.service;

import com.localhost.scoreboard.model.Game;
import com.localhost.scoreboard.model.Word;
import com.localhost.scoreboard.repository.WordRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WordService {
    private WordRepository wordRepository;

    @Autowired
    public void setWordRepository(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    static int DEFAULT_COUNT = 10;

    private Map<Integer, List<Word>> currentWords = new HashMap<>();

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

    public void initCurrentWords(Game game) throws NotFoundException {
        List<Word> words = findNotUsedByGame(game);
        if (words == null || words.size() == 0) {
            throw new NotFoundException("Can't find any unused words for game with id = " + game.getId());
        }
        Collections.shuffle(words);
        if (currentWords.containsKey(game.getId())) return;
        currentWords.put(game.getId(), words.stream().limit(DEFAULT_COUNT).collect(Collectors.toList()));
    }

    public void reinitWords(Game game) throws NotFoundException {
        currentWords.remove(game.getId());
        initCurrentWords(game);
    }

    public List<Word> getCurrentWords(Game game) {
        return currentWords.get(game.getId());
    }
}
