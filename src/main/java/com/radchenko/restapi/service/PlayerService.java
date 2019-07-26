package com.radchenko.restapi.service;

import com.radchenko.restapi.entity.Player;
import com.radchenko.restapi.entity.Team;
import com.radchenko.restapi.exception.EntityNotFoundException;
import com.radchenko.restapi.exception.InvalidRequestParametersException;
import com.radchenko.restapi.repository.PlayerRepository;
import com.radchenko.restapi.repository.TeamRepository;
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
    private final TeamRepository teamRepository;
    private final ModelMapper mapper;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, ModelMapper mapper, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.mapper = mapper;
        this.teamRepository = teamRepository;
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

    @Transactional(readOnly = true)
    public PlayerDto findOne(Long id) {
        Optional<Player> player = playerRepository.findById(id);

        return player
                .map(p -> mapper.map(p, PlayerDto.class))
                .orElseThrow(() -> new EntityNotFoundException(format("Player with id: [%s] not found", id)));
    }

    @Transactional
    public PlayerDto addPlayer(PlayerDto playerDto) {
        if (playerDto.getId() != null) {
            throw new InvalidRequestParametersException("Player should not have id field");
        }

        Player toSavePlayer = mapper.map(playerDto, Player.class);
        Player returnPlayer = playerRepository.save(toSavePlayer);

        return mapper.map(returnPlayer, PlayerDto.class);
    }

    @Transactional
    public void assignPlayerToTeam(Long playerId, Long teamId) {
        Player player = findPlayer(playerId);
        Team team = findTeam(teamId);

        player.setTeam(team);
    }

    //TODO: return a dto?
    @Transactional
    public void updatePlayer(PlayerDto playerDto) {
        if (playerDto.getId() == null) {
            throw new InvalidRequestParametersException("Player id field could  not be empty");
        }
        if (!playerRepository.existsById(playerDto.getId())) {
            throw new EntityNotFoundException(format("Player with id: %s not found", playerDto.getId()));
        }

//        Optional<Player> player = playerRepository.findById(playerDto.getId());
        //TODO: reset data from dto or just save a new Player From PlayerDto
        Player toSavePlayer = mapper.map(playerDto, Player.class);
        playerRepository.save(toSavePlayer);
    }

    @Transactional
    public void updatePlayer(Long playerId, PlayerDto playerDto) {
        if (playerDto.getId() == null) {
            throw new InvalidRequestParametersException("Player id field could  not be empty");
        }

        if (!playerId.equals(playerDto.getId())) {
            throw new InvalidRequestParametersException("Different id's value");
        }

        if (!playerRepository.existsById(playerDto.getId())) {
            throw new EntityNotFoundException(format("Player with id: %s not found", playerDto.getId()));
        }

//        Optional<Player> player = playerRepository.findById(playerDto.getId());
        //TODO: reset data from dto or just save a new Player From PlayerDto
        Player toSavePlayer = mapper.map(playerDto, Player.class);
        playerRepository.save(toSavePlayer);
    }

    @Transactional
    public void removePlayer(Long playerId) {
        playerRepository.deleteById(playerId);
    }

    private Team findTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(format("Team with id: [%s] not found", teamId)));
    }

    private Player findPlayer(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException(format("Player with id: [%s] not found", playerId)));
    }
}
