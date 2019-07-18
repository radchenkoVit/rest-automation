package com.radchenko.restapi.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

//    @Setter(AccessLevel.PRIVATE)
//    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
//    private List<Player> players = new ArrayList<>();

//    //TODO: find out why is not working setter?
//    public void addPlayer(Player player) {
//        players.add(player);
//        player.setTeam(this);
//    }
}
