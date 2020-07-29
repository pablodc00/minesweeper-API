package com.minesweeper.rest;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minesweeper.model.Cell;
import com.minesweeper.model.Game;
import com.minesweeper.service.GameService;

@RestController
@RequestMapping("/minesweeper/v1")
public class GameAPI {
	private static final Logger LOG = LoggerFactory.getLogger(GameAPI.class);
		
    @Autowired
    private GameService gameService;

    @PostMapping("/startgame")
    public ResponseEntity<Game> startgame(@RequestBody Game game) {    	
    	//Validation: amount of mines must to be less than (rows x columns) / 2
    	if (game.getNumberOfMines() > (game.getRows() * game.getColumns())) {
    		LOG.warn("Number of mines must to be less than {}", game.getRows() * game.getColumns() / 2);
    		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    	}    	
    	
    	game = this.gameService.startGame(
        		game.getUserName(), game.getRows(), game.getColumns(), game.getNumberOfMines());
        return ResponseEntity.ok(game);
    }
    
	@PutMapping("/revealcell/{row}/{column}")
    public ResponseEntity<Game> revealCell(@RequestBody Game game, @PathVariable("row") int row, 
    		@PathVariable("column") int column) {

        Optional<Cell> optcell = gameService.getCellByRowAndColumn(game, row, column);
        		        
        if (!optcell.isPresent()) {
        	LOG.warn("Not exists Cell for row: {} and column: {}", row, column);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
		
        Cell cell = optcell.get();
        if (cell.isRevealed()) {
        	LOG.warn("Cell for row: {} and column: {} is already revealed", row, column);
        	return new ResponseEntity<>(HttpStatus.IM_USED);
        }
            
    	return null;
    }
}
