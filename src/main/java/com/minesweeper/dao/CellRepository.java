package com.minesweeper.dao;

import org.springframework.data.repository.CrudRepository;

import com.minesweeper.model.Cell;

public interface CellRepository extends CrudRepository<Cell, Long> {
	Cell findByRowAndByColumn(int row, int column);
}
