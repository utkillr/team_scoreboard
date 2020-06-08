package com.localhost.scoreboard.rest;

import com.localhost.scoreboard.model.*;
import com.localhost.scoreboard.service.GameService;
import com.localhost.scoreboard.service.PlayerService;
import com.localhost.scoreboard.service.TeamService;
import com.localhost.scoreboard.util.AdminUtilities;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/player")
public class PlayerController {
    private PlayerService playerService;
    private TeamService teamService;
    private GameService gameService;

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @Autowired
    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping(value = {"", "/"})
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody List<Player> getPlayers(@RequestParam(name = "game", required = false) Integer gameId) throws NotFoundException {
        if (gameId != null && gameId != 0) {
            Game game = gameService.findById(gameId);
            if (game == null) {
                throw new NotFoundException("Can't find the game with id = " + gameId);
            }

            return game.getTeams().stream().flatMap(team -> team.getPlayers().stream()).collect(Collectors.toList());
        } else return playerService.findAll();
    }

    @GetMapping(value = {"/{id}", "/{id}/"})
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody Player getPlayer(@PathVariable(name = "id") Integer playerId) throws NotFoundException {
        Player player = playerService.findById(playerId);
        if (player == null) {
            throw new NotFoundException("Can't find the player with id = " + playerId);
        }
        return player;
    }

    @GetMapping(value = {"/{hash}/current", "/{hash}/current/"})
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody Boolean isCurrent(@PathVariable(name = "hash") String hash, @RequestParam(name = "game") Integer gameId) throws NotFoundException {
        Game game = gameService.findById(gameId);
        if (game == null) {
            throw new NotFoundException("Can't find the game with id = " + gameId);
        }
        return AdminUtilities.isCurrent(game, hash);
    }

    @GetMapping(value = {"/{hash}/admin", "/{hash}/admin/"})
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody Boolean isAdmin(@PathVariable(name = "hash") String hash) throws NotFoundException {
        return AdminUtilities.isAdmin(hash);
    }

    @GetMapping(value = {"/{hash}/id", "/{hash}/id/"})
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody Integer getId(@PathVariable(name = "hash") String hash, @RequestParam(name = "game") Integer gameId) throws NotFoundException {
        Game game = gameService.findById(gameId);
        if (game == null) {
            throw new NotFoundException("Can't find the game with id = " + gameId);
        }
        Player player = game.getTeams().stream().flatMap(team -> team.getPlayers().stream()).filter(p -> p.getHash().equals(hash)).findFirst().orElse(null);
        return player == null ? 0 : player.getId();
    }

    @PostMapping(value = {"game/{id}", "/game/{id}/"})
    @ResponseStatus(value = HttpStatus.OK)
    public synchronized @ResponseBody String postPlayer(@PathVariable(name = "id") Integer gameId, @RequestBody PlayerDAO playerDAO) throws NotFoundException {
        Game game = gameService.findById(gameId);
        if (game == null) {
            throw new NotFoundException("Can't find the game with id = " + gameId);
        }
        Player player = playerService.create(playerDAO, game.getLobby());
        game.getLobby().getPlayers().add(player);
        teamService.save(game.getLobby());
        return player.getHash();
    }
}
