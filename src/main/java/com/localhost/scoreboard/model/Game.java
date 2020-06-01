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
import java.util.List;

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

    @OneToMany(
            mappedBy = "game",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({"game"})
    private List<Team> teams;

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
        return currentTeam.getCurrentPlayer();
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
