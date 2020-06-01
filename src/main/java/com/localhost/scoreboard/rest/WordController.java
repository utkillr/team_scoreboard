package com.localhost.scoreboard.rest;

import com.localhost.scoreboard.model.Game;
import com.localhost.scoreboard.model.Word;
import com.localhost.scoreboard.model.WordUsedDAO;
import com.localhost.scoreboard.service.GameService;
import com.localhost.scoreboard.service.WordService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/word")
public class WordController {
    private WordService wordService;
    private GameService gameService;

    static int DEFAULT_COUNT = 10;

    @Autowired
    public void setWordService(WordService wordService) {
        this.wordService = wordService;
    }

    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping(value = {"", "/"})
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody List<Word> getWords(@RequestParam(name = "game", required = false) Integer gameId, @RequestParam(name = "count", required = false) Integer count) throws NotFoundException {
        if (gameId != null && gameId != 0) {
            if (count == null || count == 0) count = DEFAULT_COUNT;
            Game game = gameService.findById(gameId);
            if (game == null) {
                throw new NotFoundException("Can't find the game with id = " + gameId);
            }

            List<Word> words = wordService.findNotUsedByGame(game);
            if (words == null || words.size() == 0) {
                throw new NotFoundException("Can't find any unused words for game with id = " + gameId);
            }
            Collections.shuffle(words);
            return words.stream().limit(count).collect(Collectors.toList());
        } else return wordService.findAll();
    }

    @GetMapping(value = {"/{id}", "/{id}/"})
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody Word getWord(@PathVariable(name = "id") Integer wordId) throws NotFoundException {
        Word word = wordService.findById(wordId);
        if (word == null) {
            throw new NotFoundException("Can't find the word with id = " + wordId);
        }
        return word;
    }

    @PatchMapping(value = {"", "/"})
    @ResponseStatus(value = HttpStatus.OK)
    public void useWords(@RequestBody List<WordUsedDAO> wordUsedDAOs) throws NotFoundException, IllegalArgumentException {
        if (wordUsedDAOs == null || wordUsedDAOs.size() == 0) return;

        if (wordUsedDAOs.stream().map(WordUsedDAO::getGameId).distinct().count() != 1) {
            throw new IllegalArgumentException("Can't update words for more than one game");
        }
        if (wordUsedDAOs.stream().map(WordUsedDAO::getWordId).count() != wordUsedDAOs.size()) {
            throw new IllegalArgumentException("Can't update the same word twice");
        }

        Game game = gameService.findById(wordUsedDAOs.get(0).getGameId());
        if (game == null) {
            throw new NotFoundException("Can't find the game with id = " + wordUsedDAOs.get(0).getGameId());
        }

        List<Word> words = wordService.findAllByIds(wordUsedDAOs.stream().map(WordUsedDAO::getWordId).collect(Collectors.toList()));
        game.getUsedWords().addAll(words);
        gameService.save(game);
        wordService.save(words);
    }

}
