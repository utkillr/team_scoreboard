package com.localhost.scoreboard.service;

import com.localhost.scoreboard.model.*;
import com.localhost.scoreboard.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class GameService {

    private GameRepository gameRepository;
    private TeamService teamService;
    private WordService wordService;

    @Autowired
    public void setGameRepository(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Autowired
    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    @Autowired
    public void setWordService(WordService wordService) {
        this.wordService = wordService;
    }

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public Game findById(int id) {
        return gameRepository.findById(id);
    }

    public Game create(GameDAO gameDAO) {
        wordService.initWords();
        if (!notEmpty(gameDAO)) return null;
        Game game = new Game();
        game = gameRepository.save(game);
        game.setTeams(new ArrayList<>());
        int teamIndex = 1;
        for (TeamDAO teamDAO : gameDAO.getTeams()) {
            if (teamDAO.getName() == null || teamDAO.getName().isEmpty()) {
                teamDAO.setName("Team " + teamIndex++);
            }
            Team team = teamService.create(teamDAO, game);
            if (team != null) game.getTeams().add(team);
        }
        game = gameRepository.save(game);
        updateNextPlayer(game, true);
        return gameRepository.save(game);
    }

    private boolean notEmpty(GameDAO gameDAO) {
        for (TeamDAO teamDAO : gameDAO.getTeams()) {
            if (teamService.notEmpty(teamDAO)) {
                return true;
            }
        }
        return false;
    }

    public Game update(Game game, GameDAO gameDAO) {
        updateNextPlayer(game, gameDAO.isNext());
        for (Team team : game.getTeams()) {
            for (TeamDAO teamDAO : gameDAO.getTeams()) {
                if (team.getId() == teamDAO.getId()) {
                    teamService.update(team, teamDAO);
                }
            }
        }
        return save(game);
    }

    public Game save(Game game) {
        for (Team team : game.getTeams()) {
            teamService.save(team);
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
