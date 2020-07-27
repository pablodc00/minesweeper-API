package com.minesweeper.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Cell {

	protected Cell() {}

	public Cell(int row, int column, boolean hasMine) {
		this.crow = row;
		this.ccolumn = column;
		this.isRevealed = false;
		this.hasMine = hasMine;
		this.flag = Flag.NONE;
	}
	
    public enum Flag {
        NONE, QUESTION_MARK, RED_FLAG;
    } 	
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int crow;
    private int ccolumn;
    private boolean isRevealed;
    private boolean hasMine;
    @Enumerated(EnumType.STRING)
    private Flag flag;

}

