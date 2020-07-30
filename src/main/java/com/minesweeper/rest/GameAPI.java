package com.minesweeper.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.minesweeper.model.Cell;
import com.minesweeper.model.Game;
import com.minesweeper.service.CellService;
import com.minesweeper.service.GameService;

@RestController
@RequestMapping("/minesweeper/v1")
public class GameAPI {
	private static final Logger LOG = LoggerFactory.getLogger(GameAPI.class);
		
    @Autowired
    private GameService gameService;
    
    @Autowired
    private CellService cellService;    

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

		System.out.println("Game " + game.getId() + ", " + game.getUserName());
		LOG.info("Game " + game.getId() + ", " + game.getUserName());
		
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
       
        
        game = gameService.revealCell(game, cell);
        return ResponseEntity.ok(game);
    }
	
	@GetMapping("/resumegame")
	public ResponseEntity<Game> resumeGame(@RequestParam Long gameId, @RequestParam String userName) {
		Game game = gameService.resumeGame(gameId, userName);
		if (null == game) {
			LOG.warn("Game not found for gameId: {} and userName: {}", gameId, userName);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
		
		return ResponseEntity.ok(game);
	}
	
	@GetMapping("/getgames")
	public ResponseEntity<List<Game>> getGamesPerUser(@RequestParam String userName) {
		List<Game> games = gameService.getGamesPerUser(userName);
		if (games.isEmpty()) {
			LOG.warn("Not games found for userName: {}", userName);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(games);
	}
	
	@PutMapping("/markcell/{gameId}/{row}/{column}/{mark}")
	public ResponseEntity<Game> markCell(@PathVariable("gameId") Long gameId, @PathVariable("row") int row,
			@PathVariable("column") int column, @PathVariable("mark") Cell.Flag mark) {
		
		// find game
		Optional<Game> optgame = gameService.getGameById(gameId);
		if (!optgame.isPresent()) {
			LOG.warn("Not games found for gameId: {}", gameId);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// find cell
		Game game = optgame.get();
		Optional<Cell> optcell = gameService.getCellByRowAndColumn(game, row, column);
        if (!optcell.isPresent()) {
        	LOG.warn("Not exists Cell for row: {} and column: {}", row, column);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
		
        Cell cell = optcell.get();
        cell.setFlag(mark);
        cellService.updateCell(cell);
        
        game.getCells()
        	.stream()
        	.filter(c -> c.getId()==cell.getId())
        	.peek(c -> c.setFlag(mark))
        	.collect(Collectors.toList());
		
        return ResponseEntity.ok(optgame.get());
	}
}
