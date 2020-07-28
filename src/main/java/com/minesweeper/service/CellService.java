package com.minesweeper.service;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minesweeper.dao.CellRepository;
import com.minesweeper.model.Cell;

@Service
public class CellService {
	private static final Logger LOG = LoggerFactory.getLogger(CellService.class);
	
    @Autowired
    private CellRepository cellRepository;

    /**
     * Generate and initialize cells for a new Game
     * @param rows
     * @param columns
     * @param mines
     * @return List of new cells
     */
    public List<Cell> createCellsForNewGame(int rows, int columns, int amountOfMines) {
    	LOG.info("creating Cells for new Game - rows: {}, columns: {}, mines: {}",
    			rows, columns, amountOfMines);
    	
    	List<Point> minesPoints = this.getMines(rows, columns, amountOfMines);
    	
    	List<Cell> cellsList = new ArrayList<>();
    	Cell cell = null;
    	for (int i = 0; i < rows; i++) {
    		for (int j = 0; j < columns; j++) {
    			cell = new Cell(i, j, minesPoints.contains(new Point(i, j)));
    			cellRepository.save(cell);
    			cellsList.add(cell);
    		}
		}
    	    	
    	return cellsList;
    }

    /**
     * Generate random points where mines will be placed
     * @param rows
     * @param columns
     * @param amountOfMines
     * @return List of points
     */
    private List<Point> getMines(int rows, int columns, int amountOfMines) {
    	LOG.info("Generate random points where mines will be placed - rows: {}, columns: {}, amountOfMines: {}",
    			rows, columns, amountOfMines);

    	List<Point> points = new ArrayList<>();
    	Random rand = new Random();
    	Point point = null;
    	for (int i = 0; i < amountOfMines; i++) {
			point = new Point(rand.nextInt(rows-1), rand.nextInt(columns-1));
			if (!points.contains(point)) {
				points.add(point);
			} else {
				i--;
			}    			
    	}	
    	return points;
    }
    
    /**
     * Given a Cell and a list of its adjacent cells,
     * updates amount of adjacent mines in the given cell.
     * @param cell
     * @param adjacentCells
     */
    public void setAmountOfAdjacentMines(Cell cell, List<Cell> adjacentCells) {
    	long mines = adjacentCells
    			.stream()
    			.filter(c -> c.isHasMine())
    			.count();
		
    	cell.setAmountAdjacentMines(mines);
    	cellRepository.save(cell);    	
    }

    /**
     * Ability to 'flag' a cell with a question mark or red flag
     * @param cell
     */
    public void updateCell(Cell cell) {
    	cellRepository.save(cell);
    }
    
}
