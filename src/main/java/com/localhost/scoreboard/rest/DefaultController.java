package com.localhost.scoreboard.rest;

import com.localhost.scoreboard.model.*;
import com.localhost.scoreboard.service.AdminService;
import com.localhost.scoreboard.util.HashUtilities;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping
public class DefaultController {

    private AdminService adminService;
    private GameController gameController;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
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
    public String getGame(Model model, @PathVariable(name = "id") Integer gameId, @RequestParam(name = "hash", required = false) String hash) throws NotFoundException {
        Game game = gameController.getGame(gameId);
        boolean admin = false;
        if (adminService.isAdmin(hash)) admin = true;
        model.addAttribute("game", game);
        model.addAttribute("admin", admin);
        if (game.getStartDate() != null) {
            model.addAttribute("count", game.getTeams().size());
            return "game";
        } else {
            return "lobby";
        }
    }

    @GetMapping(value = {"game/{id}/new", "game/{id}/new/"})
    @ResponseStatus(value = HttpStatus.OK)
    public String getGameNewPlayer(Model model, @PathVariable(name = "id") Integer gameId) throws NotFoundException {
        Game game = gameController.getGame(gameId);
        model.addAttribute("game", game);
        return "new_player";
    }

    @GetMapping(value = {"login", "login/"})
    @ResponseStatus(value = HttpStatus.OK)
    public String login(Model model) {
        return "login";
    }
}
