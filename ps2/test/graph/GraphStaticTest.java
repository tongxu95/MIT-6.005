/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for static methods of Graph.
 * 
 * To facilitate testing multiple implementations of Graph, instance methods are
 * tested in GraphInstanceTest.
 */
public class GraphStaticTest {
    
    // Testing strategy
    //   empty()
    //     no inputs, only output is empty graph
    //     observe with vertices()
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testEmptyVerticesEmpty() {
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.empty().vertices());
    }
    
    // test other vertex label types in Problem 3.2
    @Test
    public void testIntegerLabels() {
    	final int v1 = 1;
    	final int v2 = 2;
    	final int v3 = 3;
    	Graph<Integer> graph = Graph.empty();
    	assertTrue("graph does not include a vertex with the given label", graph.add(v1));
    	assertEquals("one vertex in graph", graph.vertices().size(), 1);
    	graph.set(v1, v2, 5);
    	graph.set(v1, v3, 10);
    	assertEquals("three vertices in graph", graph.vertices().size(), 3);
    	
    	Map <Integer, Integer> targetv1 = new HashMap <>();
    	targetv1.put(v2, 5);
    	targetv1.put(v3, 10);
    	Map <Integer, Integer> sourcev2 = new HashMap <>();
    	sourcev2.put(v1, 5);
    	assertEquals("graph contains two edges from v1", graph.targets(v1), targetv1);
    	assertEquals("graph contains one edge to v2", graph.sources(v2), sourcev2);
    	
    	graph.remove(v3);
    	assertEquals("two vertices in graph", graph.vertices().size(), 2);
    	Map <Integer, Integer> targetv1new = new HashMap <>();
    	targetv1new.put(v2, 5);
    	assertEquals("graph contains two edges from v1", graph.targets(v1), targetv1new);
    }
    
    @Test
    public void testCharacterLabels() {
    	final char v1 = 'a';
    	final char v2 = 'b';
    	final char v3 = 'c';
    	Graph<Character> graph = Graph.empty();
    	assertTrue("graph does not include a vertex with the given label", graph.add(v1));
    	assertEquals("one vertex in graph", graph.vertices().size(), 1);
    	graph.set(v1, v2, 5);
    	graph.set(v1, v3, 10);
    	assertEquals("three vertices in graph", graph.vertices().size(), 3);
    	
    	Map <Character, Integer> targetv1 = new HashMap <>();
    	targetv1.put(v2, 5);
    	targetv1.put(v3, 10);
    	Map <Character, Integer> sourcev2 = new HashMap <>();
    	sourcev2.put(v1, 5);
    	assertEquals("graph contains two edges from v1", graph.targets(v1), targetv1);
    	assertEquals("graph contains one edge to v2", graph.sources(v2), sourcev2);
    	
    	graph.remove(v3);
    	assertEquals("two vertices in graph", graph.vertices().size(), 2);
    	Map <Character, Integer> targetv1new = new HashMap <>();
    	targetv1new.put(v2, 5);
    	assertEquals("graph contains two edges from v1", graph.targets(v1), targetv1new);
    }
    
}
