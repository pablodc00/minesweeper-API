package com.minesweeper.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minesweeper.dao.GameRepository;
import com.minesweeper.model.Cell;
import com.minesweeper.model.Game;

@Service
public class GameService {
	private static final Logger LOG = LoggerFactory.getLogger(GameService.class);
	
    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private CellService cellService;    
	
    public Game startGame(String userName, int rows, int columns, int mines) {
    	LOG.info("creating new Game");
    	List<Cell> cells = cellService.createCellsForNewGame(rows, columns, mines);
    	Game game = new Game(rows, columns, userName, cells);
    	LOG.info("presisting new Game");
    	gameRepository.save(game);
    	return game;
    }
    
}
