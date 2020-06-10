package com.localhost.scoreboard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;

@Data
@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @JsonProperty("id")
    private int id;

    @Column(
            name = "login",
            unique = true
    )
    @JsonIgnore
    private String login;

    @Column(
            name = "pwd_hash"
    )
    @JsonIgnore
    private String pwdHash;
}
