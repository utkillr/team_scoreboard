package com.localhost.scoreboard.rest;

import com.localhost.scoreboard.model.Game;
import com.localhost.scoreboard.model.GameDAO;
import com.localhost.scoreboard.model.Player;
import com.localhost.scoreboard.model.Team;
import com.localhost.scoreboard.service.AdminService;
import com.localhost.scoreboard.service.GameService;
import com.localhost.scoreboard.service.PlayerService;
import com.localhost.scoreboard.service.WordService;
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

    private AdminService adminService;
    private GameService gameService;
    private PlayerService playerService;
    private WordService wordService;

    static int MAX_TEAMS = 4;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @Autowired
    public void setWordService(WordService wordService) {
        this.wordService = wordService;
    }

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
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
        }
        return game;
    }

    @PostMapping(value = {"init", "init/"})
    @ResponseStatus(value = HttpStatus.OK)
    public int initGame(@RequestParam (name = "hash", required = false) String hash) throws NotFoundException {
        wordService.initWords();
        if (!adminService.isAdmin(hash)) return 0;
        Game game = new Game();
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
        wordService.initCurrentWords(game);
        return game.getId();
    }

    @PostMapping(value = {"{id}/start", "{id}/start/"})
    @ResponseStatus(value = HttpStatus.OK)
    public int startGame(@PathVariable(name = "id") Integer gameId, @RequestBody GameDAO gameDAO, @RequestParam(name = "hash", required = false) String hash) throws NotFoundException {
        if (!adminService.isAdmin(hash)) return 0;
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
        if (!adminService.isAdmin(hash)) return;
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
        if (!playerService.isCurrent(game, hash) && !adminService.isAdmin(hash)) return;
        long cur = System.currentTimeMillis();
        System.out.println("Activate: " + cur);
        game.setActDate(new Timestamp(cur));
        gameService.save(game);
    }

    @PatchMapping(value = {"{id}/deactivate", "{id}/deactivate/"})
    @ResponseStatus(value = HttpStatus.OK)
    public void deactivateGame(@PathVariable(name = "id") Integer gameId, @RequestParam(name = "hash", required = false) String hash) throws NotFoundException {
        Game game = gameService.findById(gameId);
        if (game == null) {
            throw new NotFoundException("Can't find the game with id = " + gameId);
        }
        if (!adminService.isAdmin(hash)) return;
        System.out.println("Deactivate: " + System.currentTimeMillis());
        game.setActDate(null);
        gameService.save(game);
    }

    @DeleteMapping(value = {"{id}", "{id}/"})
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteGame(@PathVariable(name = "id") Integer gameId, @RequestParam(name = "hash", required = false) String hash) throws NotFoundException {
        if (!adminService.isAdmin(hash)) return;
        Game game = gameService.findById(gameId);
        if (game == null) {
            throw new NotFoundException("Can't find the game with id = " + gameId);
        }

        gameService.delete(game);
    }
}
