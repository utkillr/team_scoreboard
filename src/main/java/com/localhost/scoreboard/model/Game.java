package com.localhost.scoreboard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.localhost.scoreboard.util.TimestampSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "game")
public class Game {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @JsonProperty("id")
    private int id;

    @Column(
            name = "startDate"
    )
    @JsonProperty("startDate")
    @JsonSerialize(using = TimestampSerializer.class)
    private Timestamp startDate;

    @Column(
            name = "endDate"
    )
    @JsonProperty("endDate")
    @JsonSerialize(using = TimestampSerializer.class)
    private Timestamp endDate;

    @Column(
            name = "running"
    )
    @JsonProperty("running")
    private Boolean running;

    @OneToMany(
            mappedBy = "game",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<Team> teams;

    @JsonProperty("teams")
    @JsonIgnoreProperties({"game"})
    public List<Team> getTeams() {
        return startDate == null ?
                teams.stream().filter(team -> !team.getIsLobby()).sorted(Comparator.comparing(Team::getId)).collect(Collectors.toList()) :
                teams.stream().filter(team -> !team.getIsLobby() && !team.isEmpty()).sorted(Comparator.comparing(Team::getId)).collect(Collectors.toList());
    }

    @JsonIgnore
    public Team getLobby() {
        return teams.stream().filter(Team::getIsLobby).findAny().orElse(null);
    }

    @JsonIgnore
    public List<Team> getAllTeams() {
        return teams;
    }

    @ManyToOne
    @JoinColumn(
            name = "current_team_id",
            foreignKey = @ForeignKey(name = "game_fk0")
    )
    @EqualsAndHashCode.Exclude
    @JsonProperty("currentTeamId")
    @JsonIgnoreProperties({"game", "score", "players", "currentPlayer", "current"})
    private Team currentTeam;

    @ManyToMany
    @JoinTable(
            name = "word_used",
            joinColumns = { @JoinColumn(name = "game_id") },
            inverseJoinColumns = { @JoinColumn(name = "word_id") }
    )
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<Word> usedWords;

    @JsonProperty("currentPlayer")
    @JsonIgnoreProperties({"name", "team", "current"})
    public Player getCurrentPlayer() {
        return currentTeam == null ? null : currentTeam.getCurrentPlayer();
    }

    @JsonProperty("isActive")
    public boolean isActive() {
        return endDate == null ||  endDate.compareTo(new Timestamp(System.currentTimeMillis())) >= 0;
    }

    public String serializedStartDate() {
        return TimestampSerializer.format(startDate);
    }

    public String serializedEndDate() {
        return TimestampSerializer.format(endDate);
    }
}
