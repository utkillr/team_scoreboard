package com.localhost.scoreboard.rest;

import com.localhost.scoreboard.model.Game;
import com.localhost.scoreboard.service.GameService;
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
    private GameController gameController;

    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @Autowired
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    @GetMapping(value = {"", "/"})
    @ResponseStatus(value = HttpStatus.OK)
    public String getDefault(Model model) {
        return "new";
    }

    @GetMapping(value = {"game/{id}", "game/{id}/"})
    @ResponseStatus(value = HttpStatus.OK)
    public String getGame(Model model, @PathVariable(name = "id") Integer gameId) throws NotFoundException {
        Game game = gameController.getGame(gameId);
        int count = game.getTeams().size();
        model.addAttribute("game", game);
        model.addAttribute("count", count);
        return "game";
    }
}
