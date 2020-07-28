package com.minesweeper.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.minesweeper.model.Game;

public interface GameRepository extends CrudRepository<Game, Long> {
	List<Game> findByUserName(String userName);	
	Game findByIdAndUserName(Long id, String userName);
}

