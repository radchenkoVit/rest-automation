package com.radchenko.restapi.controller;

import com.radchenko.restapi.service.TeamService;
import com.radchenko.restapi.ui.response.TeamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
