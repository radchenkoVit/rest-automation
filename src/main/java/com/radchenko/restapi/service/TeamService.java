package com.radchenko.restapi.service;

import com.radchenko.restapi.entity.Team;
import com.radchenko.restapi.exception.EntityNotFoundException;
import com.radchenko.restapi.repository.TeamRepository;
import com.radchenko.restapi.ui.response.TeamDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final ModelMapper mapper;

    public TeamService(TeamRepository teamRepository, ModelMapper mapper) {
        this.teamRepository = teamRepository;
        this.mapper = mapper;
    }

    public List<TeamDto> getAll() {
        return teamRepository.findAll()
                .stream()
                .map(t -> mapper.map(t, TeamDto.class))
                .collect(Collectors.toList());
    }

    public TeamDto findById(Long id) {
        Optional<Team> team = teamRepository.findById(id);

        return team.map(t -> mapper.map(t, TeamDto.class))
                .orElseThrow(() -> new EntityNotFoundException(format("Team with id: %s not found", id)));
    }

    public TeamDto findByName(String name) {
        Optional<Team> team = teamRepository.findTeamByName(name.trim());

        return team.map(t -> mapper.map(t, TeamDto.class))
                .orElseThrow(() -> new EntityNotFoundException(format("Team with name: %s not found", name)));
    }

    public List<TeamDto> findAllByNameContains(String partOfName) {
        List<Team> teams = teamRepository.findAllByNameContains(partOfName);

        return teams.stream()
                .map(t -> mapper.map(t, TeamDto.class))
                .collect(Collectors.toList());
    }
}
