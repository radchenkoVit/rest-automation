package com.radchenko.restapi.repository;

import com.radchenko.restapi.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findTeamByName(String name);

    List<Team> findAllByNameContains(String partOfName);
}
