package com.radchenko.restapi.controller;

import com.radchenko.restapi.exception.InvalidRequestParametersException;
import com.radchenko.restapi.service.TeamService;
import com.radchenko.restapi.ui.response.TeamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.springframework.util.StringUtils.isEmpty;

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

    //TODO: think is it good?
    @GetMapping(path = "/find")
    public List<TeamDto> find(@RequestParam(name = "name", required = false) String name,
                                    @RequestParam(name = "partOfName", required = false) String partOfName) {

        if (!isEmpty(name)) {
            return singletonList(teamService.findByName(name));
        }

        if (!isEmpty(partOfName)) {
            return teamService.findAllByNameContains(partOfName);
        }

        throw new InvalidRequestParametersException("No known parameters is sent");
    }
}
