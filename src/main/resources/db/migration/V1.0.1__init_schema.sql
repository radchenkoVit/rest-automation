DROP TABLE if EXISTS teams;
DROP TABLE if EXISTS players;

create table teams (
  id IDENTITY,
  name VARCHAR(100) NOT NULL UNIQUE,
  captain_id BIGINT UNIQUE,
  CONSTRAINT team_PK PRIMARY KEY (id)
);

create table players (
  id IDENTITY,
  full_name VARCHAR(255) NOT NULL,
  position VARCHAR(100) NOT NULL,
  team_id BIGINT,
  CONSTRAINT players_PK PRIMARY KEY (id),
  CONSTRAINT players_FK FOREIGN KEY (team_id) REFERENCES teams
);