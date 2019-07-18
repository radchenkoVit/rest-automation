package com.radchenko.restapi.service;

import com.radchenko.restapi.entity.Player;
import com.radchenko.restapi.repository.PlayerRepository;
import com.radchenko.restapi.ui.response.PlayerDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final ModelMapper mapper;

    public PlayerService(PlayerRepository playerRepository, ModelMapper mapper) {
        this.playerRepository = playerRepository;
        this.mapper = mapper;
    }

    public List<PlayerDto> getAll() {
        List<Player> players = playerRepository.findAll();

        return players.stream()
                .map(p -> mapper.map(p, PlayerDto.class))
                .collect(Collectors.toList());
    }
}
