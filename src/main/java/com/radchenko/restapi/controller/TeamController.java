package com.radchenko.restapi.controller;

import com.radchenko.restapi.service.TeamService;
import com.radchenko.restapi.ui.response.TeamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/v1/team")
@RestController
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public List<TeamDto> getAll() {
        return teamService.getAll();
    }

    @GetMapping(path = "/{id}")
    public TeamDto findById(@PathVariable Long id) {
        return teamService.findById(id);
    }

    //TODO: how to razlulit eto
    //TODO: add exception handler --> response entity, bla bla
    @GetMapping(path = "/find")
    public TeamDto findByName(@RequestParam(name = "name", required = false) String name) {
        return teamService.findByName(name);
    }

    @GetMapping(path = "/findByNames")
    public List<TeamDto> findAllByNameContains(@RequestParam(name = "partOfName", required = false) String partOfName) {
        return teamService.findAllByNameContains(partOfName);
    }
}
