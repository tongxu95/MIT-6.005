/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.Test;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    
    // Test Strategies
	// input string: empty, one line, multiple lines
	// vertices are case insensitive: one test case
	// available bridge words: 0, 1, n 
	// maximum weight: unique or not
	// output string: one test case (input words in original case, bridge words in lower case)
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    // input string: multiple lines
    // vertices are case insensitive (input string randomly capitalized)
    // bridge words = 1
    // output string letter case
    public void testOneBridge() throws IOException {
    	GraphPoet graph = new GraphPoet(new File("test/poet/test1.txt"));
    	String input = "Seek to explore new and exciting synergies!";
    	assertEquals("correct output poem", "Seek to explore strange new life and exciting synergies!",
    			graph.poem(input)); 	
    }
    
    @Test
    // input string: single line
    // bridge words: n (unique maximum weight)
    public void testMultipleBridgeUnique() throws IOException {
    	GraphPoet graph = new GraphPoet(new File("test/poet/test2.txt"));
    	String input = "w1 w2,";
    	assertEquals("correct output poem", "w1 b1 w2,",
    			graph.poem(input)); 	
    }
}
