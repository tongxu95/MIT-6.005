/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;



/**
 * A mutable data type representing the minesweeper board
 */
public class Board {
	
	private final List<List<Square>> board = Collections.synchronizedList(new ArrayList<>());
	private final int ROW;
	private final int COL;
    
    // Abstraction function
	//     represent a minesweeper board
	// Rep invariant
	//     each row must have the same number of column
	//     number of rows == row
	// Rep exposure
	//	   All field are private and final
	//     ROW and COL are integer, so guarantees immutability
	//     board is a mutable list containing mutable lists, but is encapsulated in this object 
	//	   and never exposed to a client. All parameters and return types for public methods are
	//     immutable.
	// Thread safety 
	//	   ROW and COL are immutable
	//	   board points to a threadsafe list; all access to board happen within Board methods,
	//	   which are guarded by the board's lock
	
    /**
     * Create minesweeper board, randomly assign each square to contain a bomb with
     * probability of 0.25; all squares' states are set to "-" for untouched.
     * @param row number of rows in the board
     * @param col number of columns in the board
     */
	public Board(int row, int col) {
		ROW = row;
		COL = col;
		for (int i = 0; i < row; i++) {
			List<Square> thisRow = Collections.synchronizedList(new ArrayList<>());
			for (int j = 0; j < col; j++) {
				if(new Random().nextDouble() <= 0.25) {
					thisRow.add(new Square("-", 1));
				} else {
					thisRow.add(new Square("-", 0));
				}
			}
			board.add(thisRow);
		}		
		checkRep();
	}
	
    /**
     * Create minesweeper board, assign bombs to squares based on the input file,
     * all squares' states are set to "-" for untouched.
     * @param filename file pathname where a board has been stored 
     */
	public Board(File filename) throws IOException {
		try (BufferedReader inputStream = new BufferedReader(new FileReader(filename))) {
			String[] size = inputStream.readLine().split(" ");
			ROW = Integer.parseInt(size[1]);
			COL = Integer.parseInt(size[0]);
			String line = "";
			while ((line = inputStream.readLine()) != null) {
				List<Square> thisRow = Collections.synchronizedList(new ArrayList<>());
				String[] assignBombs = line.split(" ");
				for (String bomb : assignBombs) {
					thisRow.add(new Square("-", Integer.parseInt(bomb)));			
				}	
				board.add(thisRow);
			}
		}
		checkRep();
		
	}

	private void checkRep() {
		int numRows = 0;
		for (List<Square> row : board) {
			assert row.size() == COL;
			numRows ++;
		}
		assert numRows == ROW;
	}
    
    /**
     * Dig an untouched square on the minesweeper board. 
     * @param row the row that contains of the square (start at 0 from top-left corner)
     * @param col the column that contains the square (start at 0 from top-left corner)
     */
	public synchronized String dig (int row, int col) {
		if ((row >= 0) && (row < ROW) && (col >= 0) && (col < COL)) {
			if (board.get(row).get(col).getState() == "-") {
				if (board.get(row).get(col).dig()) {
					board.get(row).get(col).setState(0);
					updateAdjacentSquares(row, col);
					digAdjacentSquares(row, col);
					return "BOOM!";
				} else {
					int bombCount = bombsInAdjacentSquares(row, col);
					board.get(row).get(col).setState(bombCount);
					if (bombCount == 0) {
						digAdjacentSquares(row, col); 
					}			 
				}
			}
		}
		checkRep();
		return this.toString();
	}
	
    /**
     * flag an untouched square on the minesweeper board. 
     * @param row the row that contains of the square (start at 0 from top-left corner)
     * @param col the column that contains the square (start at 0 from top-left corner)
     */
	public synchronized String flag (int row, int col) {
		if ((row >= 0) && (row < ROW) && (col >= 0) && (col < COL)) {
			if (board.get(row).get(col).getState() == "-") {
				board.get(row).get(col).flag();
			}
		}
		checkRep();
		return this.toString();
	}
	
    /**
     * deflag an flagged square on the minesweeper board. 
     * @param row the row that contains of the square (start at 0 from top-left corner)
     * @param col the column that contains the square (start at 0 from top-left corner)
     */
	public synchronized String deflag (int row, int col) {
		if ((row >= 0) && (row < ROW) && (col >= 0) && (col < COL)) {
			if (board.get(row).get(col).getState() == "F") {
				board.get(row).get(col).deflag();
			}
		}
		checkRep();
		return this.toString();
	}
	
    /**
     * Update bomb count in adjacent squares after clearing a bomb
     * cleared
     * @param row the row containing the square whose bomb has been cleared.
     * 		  (start at 0 from top-left corner).
     * @param col the column containing the square whose bomb has been cleared
     *        (start at 0 from top-left corner).
     */
	private void updateAdjacentSquares(int row, int col) {
		int[] bound = AdjacentSquareBound (row, col);
		for (int i = bound[0]; i <= bound[1]; i++) {
			for (int j = bound[2]; j <= bound[3]; j++) {
				board.get(i).get(j).updateState();
			}
		}
		checkRep();
	}
	
    /**
     * dig adjacent squares if they were untouched and has no neighbor with bomb
     * @param row the row that contains of the square (start at 0 from top-left corner)
     * @param col the column that contains the square (start at 0 from top-left corner)
     */
	private void digAdjacentSquares(int row, int col) {
		int[] bound = AdjacentSquareBound (row, col);
		for (int i = bound[0]; i <= bound[1]; i++) {
			for (int j = bound[2]; j <= bound[3]; j++) {
				if (board.get(i).get(j).getState() == "-") {
					int bombCount = bombsInAdjacentSquares(i,j);
					board.get(i).get(j).setState(bombCount);
					if (bombCount == 0) {						
						digAdjacentSquares(i, j);							
					}
				}
			}
		}
		checkRep();
	}
	
    /**
     * Return whether or not adjacent squares contain bombs
     * @param row the row that contains of the square (start at 0 from top-left corner)
     * @param col the column that contains the square (start at 0 from top-left corner)
     * @return bomb count in adjacent squares 
     */
	private int bombsInAdjacentSquares(int row, int col) {
		int bombCount = 0;
		int[] bound = AdjacentSquareBound (row, col);
		for (int i = bound[0]; i <= bound[1]; i++) {
			for (int j = bound[2]; j <= bound[3]; j++) {
				bombCount += board.get(i).get(j).getBomb();
			}
		}
		return bombCount;
	}
	
    /**
     * Return bound for adjacent square 
     * @param row the row that contains of the square (start at 0 from top-left corner)
     * @param col the column that contains the square (start at 0 from top-left corner)
     * @return an integer array consisting of the lower bound for row, high bound for row,
     * 		   lower bound for column, higher bound for column
     */
	private int[] AdjacentSquareBound (int row, int col) {
		int minCol = col > 0 ? col - 1 : col;
		int maxCol = col < COL - 1 ? col + 1 : col;
		int minRow = row > 0 ? row - 1 : row;
		int maxRow = row < ROW - 1 ? row + 1 : row;
		int [] bound = {minRow, maxRow, minCol, maxCol};
		return bound;
	}
	
    /**
     * Return number of columns in the Minesweeper square
     */
	public int getColumns() {
		return COL;
	}
	
    /**
     * Return number of rows in the Minesweeper square
     */
	public int getRows() {
		return ROW;
	}
	
    /**
     * Return the string representation of the board, which consists of a series of newline-
     * separated rows of space-separated characters, which represents the states of the square.
     */
	public String toString() {
		String boardMessage = "";
		for (List<Square> row : board) {
			String rowMessage = "";
			for (Square square : row) {
				rowMessage += square.toString() + " ";
			}
			boardMessage += rowMessage.substring(0, rowMessage.length() - 1) + "\n";
		}
		return boardMessage.substring(0, boardMessage.length() - 1);
	}
		
}


/**
 * A mutable data type representing a square on the minesweeper board. 
 */
class Square {
	private String state;
	private int bomb;
	
    // Abstraction function
	//     represent a square in the minesweeper board
	// Rep invariant
	//     key in map (a square's state) must be represented by a valid character: 
	//	   " ", "-", "F", [1-8].
	//	   a key's value in map must be represented by integer of 1 for bomb or 0 for no bomb
	// Rep exposure
	//	   all fields are private and of immutable types.
	// Thread safety 
	//	   Square objects are package-private and can only be called by instance methods of a 
	// 	   Board object, which are synchronized by Board's lock.
	
    /**
     * Create a square minesweeper board
     * @param state state of the square: "-" for untouched, "F" for flagged, " " for dugged with 
     * 		  no bomb in adjacent squares, integer 1<=i<=8 for dugged with i number of bomb in 
     * 		  adjacent squares
     * @param bomb integer indicating if the square contains a bomb (1 for bomb and 0 for no bomb)
     */
	Square (String state, int bomb) {
		this.state = state;
		this.bomb = bomb;
		checkRep();
	}
	
	private void checkRep() {
		assert (state == "-") | (state == "F") | (state  == " ") | state.matches("[1-8]");
		assert (bomb == 1) | (bomb == 0);
	}
	
    /**
     * @return the state of the square
     */
	String getState() {
		return state;
	}
	
    /**
     * @return 1 if square contains bomb and 0 otherwise.
     */
	int getBomb() {
		return bomb;		
	}
	
    /**
     * Modified the state of a square from untouched to dugged with new state as either " " for 
     * no bomb in adjacent squares, or integer count between 1 and 8 for number of bombs in 
     * adjacent squares.
     * @return true if adjacent square contains bomb, false otherwise
     */
	boolean setState(int bombCount) {
		assert state == "-";
		if (bombCount == 0) {
			state = " ";
			checkRep();
			return false;
		}
		else {
			state = Integer.toString(bombCount);
			checkRep();
			return true;
		}
	}
	
    /**
     * For a dugged square whose state is an integer count between 1 - 8 for bombs in adjacent
     * squares, reduce the count by 1.
     * @return true if state has been modified and false otherwise
     */
	boolean updateState() {
		boolean modified = false;
		if (state.matches("[1-8]")) {
			if (state.matches("1")) {
				state = " ";
			} else {
				state = Integer.toString(Integer.parseInt(state) - 1);
			}
			modified = true;
		} 
		checkRep();
		return modified;		
	}
	
    /**
     * dig an untouched square
     * @return true if square contained a bomb, false otherwise 
     */
	boolean dig() {
		if (bomb == 1) {
			bomb = 0;
			checkRep();
			return true;
		}
		checkRep();
		return false;
	}
	
    /**
     * flag an untouched square
     * @return true if square contained a bomb, false otherwise 
     */
	void flag () {
		state = "F";
		checkRep(); 
	}
	
    /**
     * flag an untouched square
     * @return true if square contained a bomb, false otherwise 
     */
	void deflag () {
		state = "-";
		checkRep(); 
	}
	
    /**
     * Return the string representation of a square's state  
     */
	public String toString() {
		return state;
	}
	
	
}
