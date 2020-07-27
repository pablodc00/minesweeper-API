package com.minesweeper.dao;

import org.springframework.data.repository.CrudRepository;

import com.minesweeper.model.Cell;

public interface CellRepository extends CrudRepository<Cell, Long> {
	Cell findByCrowAndCcolumn(int row, int column);
}
