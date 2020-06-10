package com.localhost.scoreboard.rest;

import com.localhost.scoreboard.model.Game;
import com.localhost.scoreboard.model.Player;
import com.localhost.scoreboard.model.PlayerDAO;
import com.localhost.scoreboard.model.Team;
import com.localhost.scoreboard.service.PlayerService;
import com.localhost.scoreboard.service.TeamService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/team")
public class TeamController {
    private PlayerService playerService;
    private TeamService teamService;

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Autowired
    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    @PatchMapping(value = {"{id}/join", "{id}/join/"})
    @ResponseStatus(value = HttpStatus.OK)
    public synchronized @ResponseBody void join(@PathVariable(name = "id") Integer teamId, @RequestBody PlayerDAO playerDAO) throws NotFoundException {
        Team team = teamService.findById(teamId);
        if (team == null) {
            throw new NotFoundException("Can't find the team with id = " + teamId);
        }
        Game game = team.getGame();
        Player player = playerService.findByGameAndHash(game, playerDAO.getHash());
        if (player == null) {
            throw new NotFoundException("Can't find the player with hash = " + playerDAO.getHash());
        }

        player.setTeam(team);
        teamService.save(team);
    }
}