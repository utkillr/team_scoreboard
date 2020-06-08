package com.localhost.scoreboard.rest;

import com.localhost.scoreboard.model.*;
import com.localhost.scoreboard.service.GameService;
import com.localhost.scoreboard.service.PlayerService;
import com.localhost.scoreboard.util.AdminUtilities;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping
public class DefaultController {

    private GameService gameService;
    private PlayerService playerService;
    private GameController gameController;
    private WordController wordController;

    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Autowired
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    @Autowired
    public void setWordController(WordController wordController) {
        this.wordController = wordController;
    }

    @GetMapping(value = {"", "/"})
    @ResponseStatus(value = HttpStatus.OK)
    public String getDefault(Model model) {
        return "new";
    }

    @GetMapping(value = {"game/{id}", "game/{id}/"})
    @ResponseStatus(value = HttpStatus.OK)
    public String getGame(Model model, @PathVariable(name = "id") Integer gameId, @RequestParam(name = "hash", required = false) String hash) throws NotFoundException {
        Game game = gameController.getGame(gameId);
        model.addAttribute("game", game);
        if (game.getStartDate() != null) {
            int count = game.getTeams().size();
            model.addAttribute("count", count);
            return "game";
        } else {
            if (AdminUtilities.isAdmin(hash)) return "admin_lobby";
            else return "lobby";
        }
    }

    @GetMapping(value = {"game/{id}/new", "game/{id}/new/"})
    @ResponseStatus(value = HttpStatus.OK)
    public String getGameNewPlayer(Model model, @PathVariable(name = "id") Integer gameId) throws NotFoundException {
        Game game = gameController.getGame(gameId);
        model.addAttribute("game", game);
        return "new_player";
    }

    @GetMapping(value = {"game/{id}/words", "game/{id}/words/"})
    @ResponseStatus(value = HttpStatus.OK)
    public String getWords(Model model, @PathVariable(name = "id") Integer gameId) throws NotFoundException {
        Game game = gameService.findById(gameId);
        if (game == null) {
            throw new NotFoundException("Can't find the game with id = " + gameId);
        }
        model.addAttribute("game", game);
        return "words";
    }
}
