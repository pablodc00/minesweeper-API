package com.minesweeper.service;

import java.util.List;
import java.util.stream.Collectors;

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
	
    /**
     * Start a new Game
     * @param userName
     * @param rows
     * @param columns
     * @param mines
     * @return A new Game
     */
    public Game startGame(String userName, int rows, int columns, int mines) {
    	LOG.info("Creating new Game - userName: {}, rows: {}, columns: {}, mines: {}",
    			userName, rows, columns, mines);
    	List<Cell> cells = cellService.createCellsForNewGame(rows, columns, mines);
    	Game game = new Game(rows, columns, userName, cells);
    	LOG.info("persisting new Game");
    	gameRepository.save(game);
    	return game;
    }
    
    
    /**
     * Given a Game and a Cell, return list of adjacent cells and
     * @param game
     * @param cell
     * @return List of adjacent cells
     */
    public List<Cell> getAdjacentCells(Game game, Cell cell) {
    	List<Cell> adjacents = game.getCells()
    			.stream()
    			.filter(c -> 
    				(c.getCrow() == cell.getCrow()-1 && c.getCcolumn() == cell.getCcolumn()-1) ||
    				(c.getCrow() == cell.getCrow()-1 && c.getCcolumn() == cell.getCcolumn())   ||
    				(c.getCrow() == cell.getCrow()-1 && c.getCcolumn() == cell.getCcolumn()+1) ||
    				(c.getCrow() == cell.getCrow() && c.getCcolumn() == cell.getCcolumn()-1) ||
    				(c.getCrow() == cell.getCrow() && c.getCcolumn() == cell.getCcolumn()+1) ||
    				(c.getCrow() == cell.getCrow()+1 && c.getCcolumn() == cell.getCcolumn()-1) ||
    				(c.getCrow() == cell.getCrow()+1 && c.getCcolumn() == cell.getCcolumn()) ||
    				(c.getCrow() == cell.getCrow()+1 && c.getCcolumn() == cell.getCcolumn()+1))
    			.collect(Collectors.toList());
    	
    	return adjacents;
    }

}
