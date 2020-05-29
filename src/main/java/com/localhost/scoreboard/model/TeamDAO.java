package com.localhost.scoreboard.model;

import lombok.Data;

import java.util.List;

@Data
public class TeamDAO {
    int id;
    private String name;
    private int score;
    private List<PlayerDAO> players;
}
