/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

/**
 * Tests for the Board Abstract Data Type and the constituting Square Abstract Data Type.
 */
public class BoardTest {
	
    // Testing strategy: board initialized from a file
	//      test board messages: untouched, flagged, dug: bomb/no bomb (no adjacent bomb
	//							 /adjacent to bombs)
	//		command: dig, flag, deflag
	//		valid and invalid command (square out of bound, invalid state (i.e. flag a 
	//		dugged square)
	File file = new File("Board_1.txt");

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // Tests
    @Test
    public void initializeBoardFromFile() throws IOException {
    	Board board = new Board(file);
    	String boardMessage = "- - - -\n- - - -\n- - - -\n- - - -";
    	assertEquals("all squares untouched", boardMessage, board.toString());
    }
    
    @Test
    public void digSquareOutOfRange() throws IOException{
    	Board board = new Board(file);
    	board.dig(5, 0);
    	String boardMessage = "- - - -\n- - - -\n- - - -\n- - - -";
    	assertEquals("all squares untouched", boardMessage, board.toString());
    }
    
    @Test
    public void digSquareNoBombAdjacentToBomb() throws IOException{
    	Board board = new Board(file);
    	board.dig(3, 1);
      	String boardMessage = "- - - -\n- - - -\n- - - -\n- 1 - -";
      	assertEquals("dug: no bomb but adjacent to 1 bomb", boardMessage, board.toString());
    }
    
    @Test
    public void digSquareNoBombNoAdjacentBomb() throws IOException{
    	Board board = new Board(file);
    	board.dig(0, 2);
      	String boardMessage = "- 1    \n- 2 1 1\n- - - -\n- - - -";
    	assertEquals("dug: no bomb and no neighbor bombs", boardMessage, board.toString());
    }
    
    @Test
    public void digSquarewithBomb() throws IOException{
    	Board board = new Board(file);
    	board.dig(3, 1);
    	assertEquals(board.dig(2, 0), "BOOM!");
    	String boardMessage = "- - - -\n1 1 1 -\n    1 -\n    1 -";
    	assertEquals("dug: hit a bomb, bomb is cleared and board updated", 
    			boardMessage, board.toString());
    }
    
    @Test
    public void flagUntouchedSquare() throws IOException{
    	Board board = new Board(file);
    	board.flag(0, 0);
    	String boardMessage = "F - - -\n- - - -\n- - - -\n- - - -";
    	assertEquals("flagged an untouched square", 
    			boardMessage, board.toString());
    }
    
    @Test
    public void flagDugSquare() throws IOException{
    	Board board = new Board(file);
    	board.dig(3, 1);
    	board.flag(3, 1);
    	String boardMessage = "- - - -\n- - - -\n- - - -\n- 1 - -";
    	assertEquals("tried to flag an dugged square", 
    			boardMessage, board.toString());
    }
    
    @Test
    public void deflagFlaggedSquare() throws IOException{
    	Board board = new Board(file);
    	board.flag(0, 0);
    	board.deflag(0, 0);
    	String boardMessage = "- - - -\n- - - -\n- - - -\n- - - -";
    	assertEquals("deflag a flagged square", 
    			boardMessage, board.toString());
    }
}
