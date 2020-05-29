package com.localhost.scoreboard.rest;

import com.localhost.scoreboard.model.Game;
import com.localhost.scoreboard.model.GameDAO;
import com.localhost.scoreboard.model.Player;
import com.localhost.scoreboard.model.Team;
import com.localhost.scoreboard.service.GameService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "/api/game")
public class GameController {

    private GameService gameService;

    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping(value = {"", "/"})
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Game> getGames() {
        return gameService.findAll();
    }

    @GetMapping(value = {"/{id}", "/{id}/"})
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody Game getGame(@PathVariable(name = "id") Integer gameId) throws NotFoundException {
        Game game = gameService.findById(gameId);
        if (game == null) {
            throw new NotFoundException("Can't find the game with id = " + gameId);
        }
        game.getTeams().sort(Comparator.comparingInt(Team::getId));
        for (Team team : game.getTeams()) {
            team.getPlayers().sort(Comparator.comparingInt(Player::getId));
        }
        gameService.updateNextPlayer(game, false);
        return game;
    }

    @PostMapping(value = {"", "/"})
    @ResponseStatus(value = HttpStatus.OK)
    public int postGame(@RequestBody GameDAO gameDAO) {
        return gameService.create(gameDAO).getId();
    }

    @PatchMapping(value = {"{id}", "{id}/"})
    @ResponseStatus(value = HttpStatus.OK)
    public void patchGame(@PathVariable(name = "id") Integer gameId, @RequestBody GameDAO gameDAO) throws NotFoundException {
        Game game = gameService.findById(gameId);
        if (game == null) {
            throw new NotFoundException("Can't find the game with id = " + gameId);
        }
        gameService.update(game, gameDAO);
    }

    @DeleteMapping(value = {"{id}", "{id}/"})
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteGame(@PathVariable(name = "id") Integer gameId) throws NotFoundException {
        Game game = gameService.findById(gameId);
        if (game == null) {
            throw new NotFoundException("Can't find the game with id = " + gameId);
        }

        gameService.delete(game);
    }
}
