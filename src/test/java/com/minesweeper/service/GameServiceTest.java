package com.minesweeper.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.minesweeper.dao.GameRepository;
import com.minesweeper.model.Cell;
import com.minesweeper.model.Game;


@SpringBootTest
@RunWith(SpringRunner.class)
public class GameServiceTest {
	
    @Autowired
    private GameService gameService;
    
    @MockBean
    private GameRepository gameRepositoryMock;

    private Game mockGame;
    private List<Cell> mockCellsList;
    
    
    @Before
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
    
	@Test
	public void testGetAdjacentCells() {
		mockCellsList.size();
		//for (0,0) we should get 3 adjacent cells
		Cell cell = mockCellsList
				.stream()
				.filter(c -> c.getCrow()==0 && c.getCcolumn()==0)
				.findFirst()
				.get();			
		
		List<Cell> adjacents = gameService.getAdjacentCells(mockGame, cell);
		assertEquals(adjacents.size(), 3);
		
		//for (1,1) we should get 8 adjacent cells
		cell = mockCellsList
				.stream()
				.filter(c -> c.getCrow()==1 && c.getCcolumn()==1)
				.findFirst()
				.get();			
		
		adjacents = gameService.getAdjacentCells(mockGame, cell);
		assertEquals(adjacents.size(), 8);		

	}

}
