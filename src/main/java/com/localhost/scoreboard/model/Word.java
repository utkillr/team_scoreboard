package com.localhost.scoreboard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "word")
public class Word {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @JsonProperty("id")
    private int id;

    @ManyToMany
    @JoinTable(
            name = "word_used",
            joinColumns = { @JoinColumn(name = "word_id") },
            inverseJoinColumns = { @JoinColumn(name = "game_id") }
    )
    @JsonIgnore
    private List<Game> usedInGames;

    @Column(
            name = "word"
    )
    @JsonProperty("word")
    private String word;
}
