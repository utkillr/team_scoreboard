package com.localhost.scoreboard.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "team")
public class Team {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @JsonProperty("id")
    private int id;

    @Column(
            name = "name"
    )
    @JsonProperty("name")
    private String name;

    @ManyToOne
    @JoinColumn(
            name = "game_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "team_fk0")
    )
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({"teams", "currentTeam", "currentPlayer", "isActive"})
    private Game game;

    @OneToMany(
            mappedBy = "team",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({"team"})
    private List<Player> players;

    @ManyToOne
    @JoinColumn(
            name = "current_player_id",
            foreignKey = @ForeignKey(name = "team_fk1")
    )
    @EqualsAndHashCode.Exclude
    @JsonProperty("getCurrentPlayer")
    @JsonIgnoreProperties({"name", "team", "current"})
    private Player currentPlayer;

    @Column(
            name = "score",
            nullable = false
    )
    @JsonProperty("score")
    private Integer score;

    @JsonProperty("current")
    public boolean isCurrent() {
        return game.getCurrentTeam() != null && game.getCurrentTeam().getId() == id;
    }
}
