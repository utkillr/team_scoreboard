package com.localhost.scoreboard.service;

import com.localhost.scoreboard.model.*;
import com.localhost.scoreboard.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TeamService {

    private TeamRepository teamRepository;
    private PlayerService playerService;

    @Autowired
    public void setTeamRepository(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public Team findById(int id) {
        return teamRepository.findById(id);
    }

    public Team update(Team team, TeamDAO teamDAO) {
        team.setScore(teamDAO.getScore());
        for (Player player : team.getPlayers()) {
            for (PlayerDAO playerDAO : teamDAO.getPlayers()) {
                if (player.getId() == playerDAO.getId()) {
                    playerService.update(player, playerDAO);
                }
            }
        }
        return team;
    }

    public Team save(Team team) {
        for (Player player : team.getPlayers()) {
            playerService.save(player);
        }
        return teamRepository.save(team);
    }

    public void delete(Team team) {
        teamRepository.delete(team);
    }

    public void updateNextPlayer(Team team, boolean next) {
        Player currentPlayer = team.getCurrentPlayer();
        Player newCurrentPlayer;
        List<Player> players = team.getPlayers();
        players.sort(Comparator.comparingInt(Player::getId));
        if (currentPlayer != null && !next) return;
        if (currentPlayer != null) {
            newCurrentPlayer = players.stream().filter(t -> t.getId() > currentPlayer.getId()).findFirst().orElse(null);
            if (newCurrentPlayer == null) newCurrentPlayer = players.get(0);
        } else {
            newCurrentPlayer = players.get(0);
        }
        team.setCurrentPlayer(newCurrentPlayer);
        teamRepository.save(team);
    }

    public Player nextPlayer(Team team) {
        Player currentPlayer = team.getCurrentPlayer();
        Player newCurrentPlayer;
        List<Player> players = team.getPlayers();
        players.sort(Comparator.comparingInt(Player::getId));
        if (currentPlayer != null) {
            newCurrentPlayer = players.stream().filter(p -> p.getId() > currentPlayer.getId()).findFirst().orElse(null);
            if (newCurrentPlayer != null) return currentPlayer;
        }
        return players.get(0);
    }
}