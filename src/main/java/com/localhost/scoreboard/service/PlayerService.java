package com.localhost.scoreboard.service;

import com.localhost.scoreboard.model.Game;
import com.localhost.scoreboard.model.Player;
import com.localhost.scoreboard.model.PlayerDAO;
import com.localhost.scoreboard.model.Team;
import com.localhost.scoreboard.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    private String getHash(Player player) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((player.getName() + player.getId()).getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (Exception e) {
            return "";
        }
    }

    public Player create(PlayerDAO playerDAO, Team team) {
        if (!notEmpty(playerDAO)) return null;
        Player player = new Player();
        player.setName(playerDAO.getName());
        player.setTeam(team);
        player.setHash(getHash(player));
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

    public Player findByGameAndHash(Game game, String hash) {
        return game.getAllTeams().stream().flatMap(t -> t.getPlayers().stream()).filter(p -> p.getHash().equals(hash)).findFirst().orElse(null);
    }
}