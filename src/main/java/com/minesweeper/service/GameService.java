package com.minesweeper.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minesweeper.dao.GameRepository;
import com.minesweeper.model.Cell;
import com.minesweeper.model.Game;
import com.minesweeper.model.Game.Status;

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
    	Game game = new Game(rows, columns, mines, userName, cells);
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
    	return cellService.getAdjacentCells(cell, game.getCells());
    }

    
    /**
     * Given a game, reveal all cells
     */
    private void revealAllCells(Game game) {    	
    	List<Cell> cells = game.getCells();
    	for (Cell cell : cells) {
			if (!cell.isRevealed()) {
				cell.setRevealed(true);
				cellService.updateCell(cell);
			}
		}
    }
    
    
    /**
     * Reveal a cell following MinesWeeper rules:
     * When a cell with no adjacent mines is revealed, all adjacent squares will be revealed (and repeat)
     * Detect when game is over
     * When a cell with any adjacent mine is revealed, all squares will be revealed
     * @param game
     * @param cell
     * @return
     */
    public Game revealCell(Game game, Cell cell) {
    	//already revealed
    	if (cell.isRevealed())
    		return game;
    	
    	//cell contains mine
    	if (cell.isHasMine()) {
    		this.revealAllCells(game);
    		game.setStatus(Status.LOST);
    		game.setEndTime(LocalDateTime.now());
    		gameRepository.save(game);
    		return game;
    	}

    	cell.setRevealed(true);
    	cellService.updateCell(cell);
    	
    	
    	// if there is no mines in adjacent cells we reveal adjacen cells
    	if (cell.getAmountAdjacentMines() == 0) {
	    	List<Cell> adjacentCells = getAdjacentCells(game, cell);
	    	for (Cell adjcell : adjacentCells) {
				if (!adjcell.isRevealed() && !adjcell.isHasMine()) {
					revealCell(game, adjcell);
				}
			}
    	}
    	
    	return game;
    }

    
    /**
     * Ability to start a new game and preserve/resume the old ones
     * @param gameId
     * @param userName
     * @return
     */
    public Game resumeGame(Long gameId, String userName) {
    	return gameRepository.findByIdAndUserName(gameId, userName);
    }
    
    
    /**
     * Given a Game, a number of row and a number of column, returns a Cell object
     * @param game
     * @param row
     * @param cell
     * @return a cell if exist, empty cell if not
     */
    public Optional<Cell> getCellByRowAndColumn(Game game, int row, int column) {
    	return game.getCells()
    		.stream()
    		.filter(c -> c.getCrow()==row && c.getCcolumn()==column)
    		.findFirst();
    }
    
    /**
     * Given an user name, returns a list of its games
     * @param userName
     * @return list of games
     */ 
    public List<Game> getGamesPerUser(String userName) {
    	return gameRepository.findByUserName(userName);
    }

    public Optional<Game> getGameById(Long gameId) {
    	return gameRepository.findById(gameId);
    }
}
