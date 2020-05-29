package com.localhost.scoreboard.service;

import com.localhost.scoreboard.model.Player;
import com.localhost.scoreboard.model.PlayerDAO;
import com.localhost.scoreboard.model.Team;
import com.localhost.scoreboard.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private PlayerRepository playerRepository;

    @Autowired
    public void setPlayerRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public Player findById(int id) {
        return playerRepository.findById(id);
    }

    public Player create(PlayerDAO playerDAO, Team team) {
        if (!notEmpty(playerDAO)) return null;
        Player player = new Player();
        player.setName(playerDAO.getName());
        player.setTeam(team);
        return playerRepository.save(player);
    }

    public boolean notEmpty(PlayerDAO playerDAO) {
        if (playerDAO.getName() != null && !playerDAO.getName().isEmpty()) {
            return true;
        }
        return false;
    }

    public Player update(Player player, PlayerDAO playerDAO) {
        if (playerDAO.getName() != null && !playerDAO.getName().isEmpty()) {
            player.setName(playerDAO.getName());
        }
        return player;
    }

    public Player save(Player player) {
        return playerRepository.save(player);
    }

    public void delete(Player player) {
        playerRepository.delete(player);
    }
}