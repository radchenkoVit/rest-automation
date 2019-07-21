package com.radchenko.restapi.service;

import com.radchenko.restapi.entity.Player;
import com.radchenko.restapi.exception.EntityNotFoundException;
import com.radchenko.restapi.repository.PlayerRepository;
import com.radchenko.restapi.ui.response.LazyPlayerDto;
import com.radchenko.restapi.ui.response.PlayerDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final ModelMapper mapper;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, ModelMapper mapper) {
        this.playerRepository = playerRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<PlayerDto> getAll() {
        List<Player> players = playerRepository.findAll();
        //TODO: check lazy hibernate function
        return players.stream()
                .map(p -> mapper.map(p, PlayerDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlayerDto> getAllLazy() {
        List<Player> players = playerRepository.findAll();

        return players.stream()
                .map(p -> {
                    PlayerDto dto = new PlayerDto();

                    dto.setFullName(p.getFullName());
                    dto.setId(p.getId());
                    dto.setPosition(p.getPosition());

                    return dto;
                }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LazyPlayerDto> getAllLazyModel() {
        List<Player> players = playerRepository.findAll();

        return players.stream()
                .map(p -> {
                    LazyPlayerDto dto = new LazyPlayerDto();
                    dto.setFullName(p.getFullName());
                    dto.setPosition(p.getPosition());

                    return dto;
                }).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public PlayerDto findByName(String name) {
        Optional<Player> player = playerRepository.findByFullName(name);

        return player.map(p -> mapper.map(p, PlayerDto.class))
                .orElseThrow(() -> new EntityNotFoundException(format("Player with name: [%s] not found", name)));
    }
}
