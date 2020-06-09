package com.localhost.scoreboard.rest;

import com.localhost.scoreboard.model.Game;
import com.localhost.scoreboard.model.GameDAO;
import com.localhost.scoreboard.model.Player;
import com.localhost.scoreboard.model.Team;
import com.localhost.scoreboard.service.GameService;
import com.localhost.scoreboard.service.WordService;
import com.localhost.scoreboard.util.AdminUtilities;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/game")
public class GameController {

    private GameService gameService;
    private WordService wordService;

    static int MAX_TEAMS = 4;

    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @Autowired
    public void setWordService(WordService wordService) {
        this.wordService = wordService;
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
        // Empty if lobby
        if (game.getStartDate() != null) {
            gameService.updateNextPlayer(game, false);
            for (Team team : game.getTeams()) {
                team.getPlayers().sort(Comparator.comparingInt(Player::getId));
            }
            System.out.println("Sorted teams: " + game.getTeams().stream().map(t -> t.getName() + "(" + t.getId() + ")").collect(Collectors.joining(", ", "[", "]")));
        }
        return game;
    }

    @PostMapping(value = {"", "/"})
    @ResponseStatus(value = HttpStatus.OK)
    public int postGame(@RequestBody GameDAO gameDAO, @RequestParam (name = "hash", required = false) String hash) {
        if (!AdminUtilities.isAdmin(hash)) return 0;
        return gameService.create(gameDAO).getId();
    }

    @PostMapping(value = {"init", "init/"})
    @ResponseStatus(value = HttpStatus.OK)
    public int initGame(@RequestParam (name = "hash", required = false) String hash) throws NotFoundException {
        if (!AdminUtilities.isAdmin(hash)) return 0;
        Game game = new Game();
        game.setRunning(false);
        gameService.save(game);
        game.setTeams(new ArrayList<>());
        for (int i = 0; i < MAX_TEAMS; i++) {
            Team team = new Team();
            team.setScore(0);
            team.setName("Team " + (i + 1));
            team.setGame(game);
            team.setIsLobby(false);
            team.setPlayers(new ArrayList<>());
            game.getAllTeams().add(team);
        }
        Team team = new Team();
        team.setScore(0);
        team.setName("Lobby");
        team.setGame(game);
        team.setIsLobby(true);
        team.setPlayers(new ArrayList<>());
        game.getAllTeams().add(team);
        game = gameService.save(game);
        System.out.println("Game created with id " + game.getId());
        System.out.println("Game has " + game.getAllTeams().size() + " teams:");
        for (Team t : game.getAllTeams()) {
            System.out.println("Team " + t.getId() + ": " + t.getName());
        }
        wordService.initCurrentWords(game);
        return game.getId();
    }

    @PostMapping(value = {"{id}/start", "{id}/start/"})
    @ResponseStatus(value = HttpStatus.OK)
    public int startGame(@PathVariable(name = "id") Integer gameId, @RequestBody GameDAO gameDAO, @RequestParam(name = "hash", required = false) String hash) throws NotFoundException {
        if (!AdminUtilities.isAdmin(hash)) return 0;
        Game game = gameService.findById(gameId);
        if (game == null) {
            throw new NotFoundException("Can't find the game with id = " + gameId);
        }
        gameService.update(game, gameDAO);
        game.setStartDate(new Timestamp(System.currentTimeMillis()));
        game = gameService.save(game);
        wordService.initCurrentWords(game);
        return game.getId();
    }

    @PatchMapping(value = {"{id}", "{id}/"})
    @ResponseStatus(value = HttpStatus.OK)
    public void patchGame(@PathVariable(name = "id") Integer gameId, @RequestBody GameDAO gameDAO, @RequestParam(name = "hash", required = false) String hash) throws NotFoundException {
        if (!AdminUtilities.isAdmin(hash)) return;
        Game game = gameService.findById(gameId);
        if (game == null) {
            throw new NotFoundException("Can't find the game with id = " + gameId);
        }
        gameService.update(game, gameDAO);
    }

    @PatchMapping(value = {"{id}/activate", "{id}/activate/"})
    @ResponseStatus(value = HttpStatus.OK)
    public void activateGame(@PathVariable(name = "id") Integer gameId, @RequestParam(name = "hash", required = false) String hash) throws NotFoundException {
        Game game = gameService.findById(gameId);
        if (game == null) {
            throw new NotFoundException("Can't find the game with id = " + gameId);
        }
        if (!AdminUtilities.isCurrent(game, hash) && !AdminUtilities.isAdmin(hash)) return;
        game.setRunning(true);
        gameService.save(game);
    }

    @PatchMapping(value = {"{id}/deactivate", "{id}/deactivate/"})
    @ResponseStatus(value = HttpStatus.OK)
    public void deactivateGame(@PathVariable(name = "id") Integer gameId, @RequestParam(name = "hash", required = false) String hash) throws NotFoundException {
        Game game = gameService.findById(gameId);
        if (game == null) {
            throw new NotFoundException("Can't find the game with id = " + gameId);
        }
        if (!AdminUtilities.isAdmin(hash)) return;
        game.setRunning(false);
        gameService.save(game);
    }

    @DeleteMapping(value = {"{id}", "{id}/"})
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteGame(@PathVariable(name = "id") Integer gameId, @RequestParam(name = "hash", required = false) String hash) throws NotFoundException {
        if (!AdminUtilities.isAdmin(hash)) return;
        Game game = gameService.findById(gameId);
        if (game == null) {
            throw new NotFoundException("Can't find the game with id = " + gameId);
        }

        gameService.delete(game);
    }
}
