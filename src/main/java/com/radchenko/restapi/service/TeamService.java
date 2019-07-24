package com.radchenko.restapi.service;

import com.radchenko.restapi.entity.Team;
import com.radchenko.restapi.exception.EntityNotFoundException;
import com.radchenko.restapi.repository.PlayerRepository;
import com.radchenko.restapi.repository.TeamRepository;
import com.radchenko.restapi.ui.response.PlayerDto;
import com.radchenko.restapi.ui.response.TeamDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final ModelMapper mapper;

    @Autowired
    public TeamService(TeamRepository teamRepository, ModelMapper mapper, PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.mapper = mapper;
        this.playerRepository = playerRepository;
    }

    @Transactional(readOnly = true)
    public List<TeamDto> getAll() {
        return teamRepository.findAll()
                .stream()
                .map(t -> mapper.map(t, TeamDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TeamDto findById(Long id) {
        Optional<Team> team = teamRepository.findById(id);

        return team.map(t -> mapper.map(t, TeamDto.class))
                .orElseThrow(() -> new EntityNotFoundException(format("Team with id: %s not found", id)));
    }

    @Transactional(readOnly = true)
    public TeamDto findByName(String name) {
        Optional<Team> team = teamRepository.findTeamByName(name.trim());

        return team.map(t -> mapper.map(t, TeamDto.class))
                .orElseThrow(() -> new EntityNotFoundException(format("Team with name: %s not found", name)));
    }

    @Transactional(readOnly = true)
    public List<TeamDto> findAllByNameContains(String partOfName) {
        List<Team> teams = teamRepository.findAllByNameContains(partOfName);

        return teams.stream()
                .map(t -> mapper.map(t, TeamDto.class))
                .collect(Collectors.toList());
    }

    public PlayerDto getCaptain(Long teamId) {
        Team team = teamRepository
                .findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(format("Team with id: [%s] not found", teamId)));

        Long captainId = team.getCaptainId();

        if (captainId == null) {//TODO: left it's to show standart error message and message that was caught
            throw new RuntimeException("No captain assigned to the team");
        }

        return playerRepository
                .findById(teamId)
                .map(p -> mapper.map(p, PlayerDto.class))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Captain with id: [%s] not found", captainId)));
    }
}
