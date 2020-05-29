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
@Table(name = "player")
public class Player {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @JsonProperty("id")
    private int id;

    @ManyToOne
    @JoinColumn(
            name = "team_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "player_fk0")
    )
    @JsonIgnoreProperties({"players"})
    @EqualsAndHashCode.Exclude
    private Team team;

    @Column(
            name = "name",
            nullable = false
    )
    @JsonProperty("name")
    private String name;

    @JsonProperty("current")
    public boolean isCurrent() {
        return team.isCurrent() && team.getCurrentPlayer() != null && team.getCurrentPlayer().getId() == id;
    }
}
