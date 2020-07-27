package com.minesweeper.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.minesweeper.dao.CellRepository;
import com.minesweeper.model.Cell;


@SpringBootTest
@RunWith(SpringRunner.class)
public class CellServiceTest {
	
    @Autowired
    private CellService pedidoService;
    
    @MockBean
    private CellRepository cellRepositoryMock;

    
	@Test
	public void testCreateCellsForNewGame() {
		int rows = 6;
		int columns = 9;
		int mines = 4;
		
		when(this.cellRepositoryMock.save(any(Cell.class))).thenReturn(any(Cell.class));
		List<Cell> cellsList = pedidoService.createCellsForNewGame(rows, columns, mines);
		
		List<Cell> cellsMinesList = cellsList.stream()
				.filter(c -> c.isHasMine() == true)
				.collect(Collectors.toList());

		assertEquals(mines, cellsMinesList.size());		
	}

}
