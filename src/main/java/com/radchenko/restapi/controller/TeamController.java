package com.radchenko.restapi.controller;

import com.radchenko.restapi.exception.InvalidRequestParametersException;
import com.radchenko.restapi.service.TeamService;
import com.radchenko.restapi.ui.response.PlayerDto;
import com.radchenko.restapi.ui.response.TeamDto;
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
    public ResponseEntity<List<TeamDto>> getAll() {
        return new ResponseEntity<>(teamService.getAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TeamDto> findById(@PathVariable Long id) {
        return new ResponseEntity<>(teamService.findById(id), HttpStatus.OK);
    }

    //TODO: think is it good?
    @GetMapping(path = "/find")
    public ResponseEntity<List<TeamDto>> find(@RequestParam(name = "name", required = false) String name,
                                    @RequestParam(name = "partOfName", required = false) String partOfName) {

        if (!isEmpty(name)) {
            return new ResponseEntity<>(singletonList(teamService.findByName(name)), HttpStatus.OK);
        }

        if (!isEmpty(partOfName)) {
            return new ResponseEntity<>(teamService.findAllByNameContains(partOfName), HttpStatus.OK);
        }

        throw new InvalidRequestParametersException("No known parameters is sent");
    }

    @GetMapping(path = "/{teamId}/captain")
    public ResponseEntity<PlayerDto> getCaptain(@PathVariable Long teamId) {
        return new ResponseEntity<>(teamService.getCaptain(teamId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TeamDto> createTeam(@RequestBody TeamDto teamDto) {
        return new ResponseEntity<>(teamService.addTeam(teamDto), HttpStatus.CREATED);
    }
}
