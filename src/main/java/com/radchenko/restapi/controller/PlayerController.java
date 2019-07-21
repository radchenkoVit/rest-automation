package com.radchenko.restapi.controller;

import com.radchenko.restapi.service.PlayerService;
import com.radchenko.restapi.ui.response.LazyPlayerDto;
import com.radchenko.restapi.ui.response.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
