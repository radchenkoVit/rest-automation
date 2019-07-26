package com.radchenko.restapi.service;

import com.radchenko.restapi.entity.Player;
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
import java.util.Objects;
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

    @Transactional(readOnly = true)
    public PlayerDto getCaptain(Long teamId) {
        Team team = findTeam(teamId);
        Long captainId = team.getCaptainId();

        if (captainId == null) {//TODO: left it's to show standard error message and message that was caught
            throw new RuntimeException("No captain assigned to the team");
        }

        return playerRepository
                .findById(teamId)
                .map(p -> mapper.map(p, PlayerDto.class))
                .orElseThrow(() -> new EntityNotFoundException(format("Captain with id: [%s] not found", captainId)));
    }

    @Transactional
    //TODO:	add new team with id's of players which already exists
    //for current  case it's bug that it's create/update existing team
    public TeamDto addTeam(TeamDto teamDto) {
        //no checking
        //todo: two times with the same id --> error
        Team team = mapper.map(teamDto, Team.class);
        teamRepository.save(team);

        return mapper.map(team, TeamDto.class);
    }

    @Transactional
    public void removeTeam(Long teamId) {
        Team team = findTeam(teamId);
        team.getPlayers().forEach(player -> player.setTeam(null));//TODO: could create bug when team is deleted but players is not unassigned from tean
        teamRepository.deleteById(teamId);
    }

    @Transactional
    public void assignCaptain(Long teamId, Long captainId) {
        Player captainPlayer = findCaptain(captainId);
        Team newTeam = findTeam(teamId);

        if (captainPlayer.getTeam() != null && !Objects.equals(captainPlayer.getTeam().getId(), teamId)) {
            Team oldTeam = captainPlayer.getTeam();
            oldTeam.setCaptainId(null);

            captainPlayer.setTeam(newTeam);//TODO:try to create bug where captain could be on two teams????
            newTeam.addPlayer(captainPlayer);
        }

        newTeam.setCaptainId(captainPlayer.getId());
    }

    @Transactional
    public void assignPlayer(Long teamId, Long playerId) {
        Player player = findPlayer(playerId);
        Team team = findTeam(teamId);

        player.setTeam(team);//TODO: check if it works correct
        team.addPlayer(player);
    }

    @Transactional//TODO: check if it works correct
    public void addPlayer(Long teamId, Long playerId) {
        Team team = findTeam(teamId);
        Player player = findPlayer(playerId);

        team.addPlayer(player);
    }


    private Team findTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(format("Team with id: [%s] not found", teamId)));
    }

    private Player findCaptain(Long playerId) {
        return findPlayer(playerId,"Captain with id: [%s] not found");
    }

    private Player findPlayer(Long playerId) {
        return findPlayer(playerId,"Player with id: [%s] not found");
    }

    private Player findPlayer(Long playerId, String messagePattern) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException(format(messagePattern, playerId)));
    }
}
