package com.minesweeper.service;

import java.util.ArrayList;
import java.util.List;

import com.minesweeper.model.Cell;
import com.minesweeper.model.Game;

public abstract class ParentTest {

    protected Game mockGame;
    protected List<Cell> mockCellsList;
    
    public void initTest() {
    	mockCellsList = new ArrayList<>(9);
    	
    	//Construct a dashboard of 6 x 6 cells    	
    	Cell cell = new Cell(0, 0, false);
    	mockCellsList.add(cell);
    	
    	cell = new Cell(0, 1, false);
    	mockCellsList.add(cell);
    	
    	cell = new Cell(0, 2, true);
    	mockCellsList.add(cell);
    	
    	cell = new Cell(1, 0, true);
    	mockCellsList.add(cell);
    	
    	cell = new Cell(1, 1, false);
    	mockCellsList.add(cell);    	
    	
    	cell = new Cell(1, 2, false);
    	mockCellsList.add(cell);
    	
    	cell = new Cell(2, 0, true);
    	mockCellsList.add(cell);
    	
    	cell = new Cell(2, 1, false);
    	mockCellsList.add(cell);
    	
    	cell = new Cell(2, 2, false);
    	mockCellsList.add(cell);
    	
    	mockGame = new Game(3, 3, "dummy", mockCellsList);
    }    
	
}
