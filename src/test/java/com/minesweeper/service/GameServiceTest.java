package com.minesweeper.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.minesweeper.dao.GameRepository;
import com.minesweeper.model.Cell;


@SpringBootTest
@RunWith(SpringRunner.class)
public class GameServiceTest extends ParentTest {
	
    @Autowired
    private GameService gameService;
    
    @MockBean
    private GameRepository gameRepositoryMock;

    @Before
    public void initTest() {
    	super.initTest();
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
