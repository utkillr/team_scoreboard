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
            name = "start_date"
    )
    @JsonProperty("startDate")
    @JsonSerialize(using = TimestampSerializer.class)
    private Timestamp startDate;

    @Column(
            name = "end_date"
    )
    @JsonProperty("endDate")
    @JsonSerialize(using = TimestampSerializer.class)
    private Timestamp endDate;

    @Column(
            name = "act_date"
    )
    @JsonProperty("actDate")
    @JsonSerialize(using = TimestampSerializer.class)
    private Timestamp actDate;

    @JsonProperty("diff")
    public Long getDiff() {
        return actDate == null ? -1 : actDate.getTime() - System.currentTimeMillis() + PREPARE_TIME;
    }

    @JsonProperty("running")
    public Boolean isRunning() {
        return actDate != null;
    }

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
        return startDate != null && (endDate == null || endDate.compareTo(new Timestamp(System.currentTimeMillis())) >= 0);
    }

    public String serializedStartDate() {
        return TimestampSerializer.format(startDate);
    }

    public String serializedEndDate() {
        return TimestampSerializer.format(endDate);
    }

    private static int PREPARE_TIME = 3 * 1000;
}
