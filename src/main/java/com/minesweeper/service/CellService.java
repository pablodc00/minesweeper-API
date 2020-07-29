package com.minesweeper.service;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
    	boolean flag = false;
    	for (int i = 0; i < rows; i++) {
    		for (int j = 0; j < columns; j++) {
    			flag = minesPoints.contains(new Point(i, j));
    			cell = new Cell(i, j, flag);
    			cellRepository.save(cell);
    			cellsList.add(cell);
    		}
		}

    	//update amount of mines in adjacen cells
    	List<Cell> adjacentCellsList = new ArrayList<>();
    	for (Cell minecell : cellsList) {
			if (minecell.isHasMine()) {
				adjacentCellsList = getAdjacentCells(minecell, cellsList);
				updateAmountOfMinesInAdjacentCells(adjacentCellsList);
			}
		}
    	
    	return cellsList;
    }

    
    /**
     * Given a cell containing a mine and a list of adjacent cells,
     * update the amount of adjacent mines in each cell of the list.
     * @param adjacentCellsList
     */
    private void updateAmountOfMinesInAdjacentCells(List<Cell> adjacentCellsList) {
    	for (Cell adjcell : adjacentCellsList) {
			adjcell.setAmountAdjacentMines(adjcell.getAmountAdjacentMines()+1);
			cellRepository.save(adjcell);
		}
    }
    
    
    /**
     * Given a cell and a list of all cells, return a list o adjacent cells
     * for the given cell.
     * @param cell
     * @param cellsList
     * @return List of adjacents cells
     */
    public List<Cell> getAdjacentCells(Cell cell, List<Cell> cellsList) {
    	//TODO use cache
    	List<Cell> adjacents = cellsList
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
