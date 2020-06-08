package com.localhost.scoreboard.rest;

import com.localhost.scoreboard.model.Game;
import com.localhost.scoreboard.model.Word;
import com.localhost.scoreboard.model.WordUsedDAO;
import com.localhost.scoreboard.service.GameService;
import com.localhost.scoreboard.service.WordService;
import com.localhost.scoreboard.util.AdminUtilities;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/word")
public class WordController {

    private WordService wordService;
    private GameService gameService;

    @Autowired
    public void setWordService(WordService wordService) {
        this.wordService = wordService;
    }

    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
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

    @GetMapping(value = {"", "/"})
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody List<Word> getUnusedWords(@RequestParam(name = "game") Integer gameId, @RequestParam(name = "hash", required = false) String hash) throws NotFoundException {
        Game game = gameService.findById(gameId);
        if (game == null) {
            throw new NotFoundException("Can't find the game with id = " + gameId);
        }
        if (AdminUtilities.isCurrent(game, hash) && game.getRunning() || AdminUtilities.isAdmin(hash)) {
            if (wordService.getCurrentWords(game) == null || wordService.getCurrentWords(game).isEmpty()) {
                wordService.initCurrentWords(game);
            }
            return wordService.getCurrentWords(game);
        }
        else return new ArrayList<>();
    }

    @PatchMapping(value = {"", "/"})
    @ResponseStatus(value = HttpStatus.OK)
    public void useWords(@RequestBody List<WordUsedDAO> wordUsedDAOs, @RequestParam(name = "hash", required = false) String hash) throws NotFoundException, IllegalArgumentException {
        if (!AdminUtilities.isAdmin(hash)) return;

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
        wordService.reinitWords(game);
    }
}
