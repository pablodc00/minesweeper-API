package com.minesweeper.dao;

import org.springframework.data.repository.CrudRepository;

import com.minesweeper.model.Game;

public interface GameRepository extends CrudRepository<Game, Long> {

}

