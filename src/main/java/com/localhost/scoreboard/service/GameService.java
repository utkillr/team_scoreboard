package com.localhost.scoreboard.service;

import com.localhost.scoreboard.model.*;
import com.localhost.scoreboard.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

    private GameRepository gameRepository;
    private TeamService teamService;

    @Autowired
    public void setGameRepository(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Autowired
    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public Game findById(int id) {
        return gameRepository.findById(id);
    }

    public Game update(Game game, GameDAO gameDAO) {
        updateNextPlayer(game, gameDAO.isNext());
        for (Team team : game.getAllTeams()) {
            for (TeamDAO teamDAO : gameDAO.getTeams()) {
                if (team.getId() == teamDAO.getId()) {
                    teamService.update(team, teamDAO);
                }
            }
        }
        return save(game);
    }

    public Game save(Game game) {
        if (game.getAllTeams() != null) {
            for (Team team : game.getAllTeams()) {
                teamService.save(team);
            }
        }
        return gameRepository.save(game);
    }

    public void delete(Game game) {
        gameRepository.delete(game);
    }

    public void updateNextPlayer(Game game, boolean next) {
        Team currentTeam = game.getCurrentTeam();
        Team newCurrentTeam;
        List<Team> teams = game.getTeams();
        teams.sort(Comparator.comparingInt(Team::getId));
        if (currentTeam != null && !next) return;
        if (currentTeam != null) {
            newCurrentTeam = teams.stream().filter(t -> t.getId() > currentTeam.getId()).findFirst().orElse(null);
            if (newCurrentTeam == null) newCurrentTeam = teams.get(0);
        } else {
            newCurrentTeam = teams.get(0);
        }

        teamService.updateNextPlayer(newCurrentTeam, next);
        game.setCurrentTeam(newCurrentTeam);
        gameRepository.save(game);
    }
}
