package com.radchenko.restapi.controller;

import com.radchenko.restapi.service.PlayerService;
import com.radchenko.restapi.ui.response.LazyPlayerDto;
import com.radchenko.restapi.ui.response.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/v1/player")
@RestController
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public List<PlayerDto> getAll() {
        return playerService.getAll();
    }

    @GetMapping(path = "/lazy")
    public List<PlayerDto> getAllLazy() {
        return playerService.getAllLazy();
    }

    @GetMapping(path = "/lazy2")
    public List<LazyPlayerDto> getAllLazyModel() {
        return playerService.getAllLazyModel();
    }

    @GetMapping(path = "/find")
    public PlayerDto findByName(@RequestParam(name = "name") String name) {
        return playerService.findByName(name);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PlayerDto> getOne(@PathVariable Long id) {
        return new ResponseEntity<>(playerService.findOne(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PlayerDto> createPlayer(@RequestBody PlayerDto playerDto) {
        return new ResponseEntity<>(playerService.addPlayer(playerDto), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{playerId}/assign")
    public ResponseEntity<Void> assignTeam(@PathVariable Long playerId, @RequestParam Long teamId) {
        playerService.assignPlayerToTeam(playerId, teamId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping//TODO: looks like it works
    public ResponseEntity<Void> updatePlayer(@RequestBody PlayerDto playerDto) {
        playerService.updatePlayer(playerDto);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping(path = "/update/{id}")//TODO: looks like it works
    public ResponseEntity<Void> updatePlayer(@PathVariable Long id, @RequestBody PlayerDto playerDto) {
        playerService.updatePlayer(id, playerDto);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    public ResponseEntity removePlayer(@PathVariable Long playerId) {
        playerService.removePlayer(playerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
