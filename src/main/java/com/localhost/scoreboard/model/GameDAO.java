package com.localhost.scoreboard.model;

import lombok.Data;

import java.util.List;

@Data
public class GameDAO {
    int id;
    private List<TeamDAO> teams;
    boolean next;
}
