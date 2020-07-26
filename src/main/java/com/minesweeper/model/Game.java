package com.minesweeper.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Game {

	protected Game() {}
	
	public Game(int rows, int columns, String username, List<Cell> cells) {
		this.rows = rows;
		this.columns = columns;
		this.userName = username;
		this.cells = cells;
		this.sartTime = LocalDateTime.now();
		this.status = Status.IN_PROGRESS;
	}
	
    public enum Status {
        IN_PROGRESS, LOST, WON;
    } 	
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int rows;
    private int columns;
    private String userName;
    @OneToMany
    List<Cell> cells;
    private LocalDateTime sartTime;
    private LocalDateTime endTime;
    @Enumerated(EnumType.STRING)
    private Status status;

}
