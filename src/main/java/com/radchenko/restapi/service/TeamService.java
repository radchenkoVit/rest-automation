package com.radchenko.restapi.service;

import com.radchenko.restapi.repository.TeamRepository;
import com.radchenko.restapi.ui.response.TeamDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;

    public TeamService(TeamRepository teamRepository, ModelMapper modelMapper) {
        this.teamRepository = teamRepository;
        this.modelMapper = modelMapper;
    }

    public List<TeamDto> getAll() {
        return teamRepository.findAll().stream()
                .map(t -> modelMapper.map(t, TeamDto.class))
                .collect(Collectors.toList());
    }
}
