package com.minesweeper.service;

import java.util.List;

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

    public List<Cell> createCellsForNewGame(int rows, int columns, int mines) {
    	LOG.info("creating Cells for new Game");    	
    	//TODO
    	return null;
    }
}
