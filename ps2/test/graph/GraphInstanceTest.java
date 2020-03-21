/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
    /*
	 * Testing Strategy: 
	 * 
	 * add(v)
	 * Partition the inputs as follows:
	 * size before: 0, 1, n
	 * size after: 0, 1, n
	 * v present or not
	 * 
	 * set(e)
	 * Partition the inputs as follows:
	 * size before: 0, 1, n
	 * size after: 0, 1, n
	 * e present of not (weight changed or not)
	 * vertices for edge present or not
	 * Weight: 0, >0
	 * 
	 * remove(v)
	 * Partition the inputs as follows:
	 * size before: 0, 1, n
	 * size after: 0, 1, n
	 * v present or not
	 * removes vertex and edges
	 * 
	 * vertices()
	 * Partition the inputs as follows:
	 * size: 0, 1, n
	 * 
	 * sources(v)
	 * Partition the input as follows:
	 * number of edges to target labels: 0, 1, n
	 * number of edges in graph: 0, 1, n
	 * 
	 * targets()
	 * Partition the input as follows:
	 * number of edges from source: 0, 1, n
	 * number of edges in graph: 0, 1, n
     */
    
	private static final String v1 = "a";
	private static final String v2 = "b";
	private static final String v3 = "c";

    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    @Test
    // covers vertices(), size = 0
    public void testInitialVerticesEmpty() {
        // TODO you may use, change, or remove this test
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }
    
    @Test
    // Covers 
    // add() size before = 0, size after = 1
    // vertices() size = 1
    public void testAddVertexToEmptyGraph() {
    	Graph<String> graph = emptyInstance(); 
    	assertTrue("graph does not include a vertex with the given label", graph.add(v1));
    	assertTrue("graph contains given vertex", graph.vertices().contains(v1));
    	assertEquals("graph contains one vertex", graph.vertices().size(), 1);
    }
    
    @Test
    // Covers 
    // add() size before = 1, size after = 1, vertex already in graph
    public void testAddVertexAlreadyInGraph() {
    	Graph<String> graph = emptyInstance();  
    	graph.add(v1);    	
    	assertFalse("graph already include a vertex with the given label", graph.add(v1));
    	assertEquals("graph contains one vertex", graph.vertices().size(), 1);
    }
    
    @Test
    // Covers 
    // add() size before = n, size after = n, vertex not in graph
    // vertices() size = n
    public void testAddGraphWithMultipleVertices() {
    	Graph<String> graph = emptyInstance();  
    	graph.add(v1);
    	graph.add(v2);
    	assertEquals("graph contains two vertices", graph.vertices().size(), 2);
    	assertTrue("graph does not include a vertex with the given label", graph.add(v3));
    	assertEquals("graph contains three vertices", graph.vertices().size(), 3);
    }
    
    @Test
    // Covers 
    // set() size before = 0, size after = 1, edge and its vertices not in graph, weight > 0
    // sources() number of edges = 1, edges to target label = 1
    // targets() number of edges = 1, edges from source = 1
    public void testSetAddEdgeToEmptyGraph() {
    	Graph<String> graph = emptyInstance();    	
    	assertEquals("graph do not contain edge", graph.set(v1, v2, 5), 0);
    	assertTrue("vertices for edge added", graph.vertices().contains(v1));
    	assertTrue("vertices for edge added", graph.vertices().contains(v2));

    	Map <String, Integer> source = new HashMap <>();
    	source.put(v1, 5);
    	assertEquals("graph contain one source to target vertex", graph.sources(v2), source);
    	
    	Map <String, Integer> target = new HashMap <>();
    	target.put(v2, 5);
    	assertEquals("graph contains one target from source vertex", graph.targets(v1), target);
    }
    
    @Test
    // Covers 
    // set() size before = 1, size after = 0, edge and its vertices in graph, weight = 0
    public void testSetRemoveEdge() {
    	Graph<String> graph = emptyInstance();  
    	graph.add(v1);
    	graph.add(v2);
    	graph.set(v1, v2, 5);
    	
    	assertEquals("graph contained edge", graph.set(v1, v2, 0), 5);
    	assertTrue("edge removed", graph.sources(v2).isEmpty());
    }

    @Test
    // Covers 
    // set() size before = 0, size after = 0, weight = 0
    public void testSetNoChange() {
    	Graph<String> graph = emptyInstance();  
    	graph.set(v1, v2, 0);
    	assertTrue("graph contain no edge from v1", graph.targets(v1).isEmpty());
    	assertTrue("graph contain no edge to v2", graph.sources(v2).isEmpty());
    	assertTrue("graph contain no vertices", graph.vertices().isEmpty());
    }

    @Test
    // Covers 
    // set() size before = 1, size after = n, 
    // edge and one of its vertices not in graph, weight > 0 
    // targets() number of edges = n, edges from source label = n
    public void testSetAddEdge() {
    	Graph<String> graph = emptyInstance();  
    	graph.add(v1);
    	graph.add(v2);
    	graph.set(v1, v2, 5);
    	graph.set(v1, v3, 10);
    	
    	Map <String, Integer> target = new HashMap <>();
    	target.put(v2, 5);
    	target.put(v3, 10);
    	assertEquals("graph contains edge from source", graph.targets(v1), target);
    	assertTrue("vertices for edge added", graph.vertices().contains(v3));
    }
    
    @Test
    // Covers 
    // set() size before = n, size after = n, edge and its vertices in graph, weight > 0 (weight changed)
    // sources() number of edges = n, edges to target label = n
    public void testSetChangeEdge() {
    	Graph<String> graph = emptyInstance();  
    	graph.add(v1);
    	graph.add(v2);
    	graph.add(v3);
    	graph.set(v1, v2, 5);
    	graph.set(v3, v2, 10);
    	
    	
    	Map <String, Integer> source = new HashMap <>();
    	source.put(v1, 5);
    	source.put(v3, 10);
    	assertEquals("graph contain edges from source", graph.sources(v2), source);
    	
    	graph.set(v1, v2, 10);
    	Map <String, Integer> source2 = new HashMap <>();
    	source2.put(v1, 10);
    	source2.put(v3, 10);
    	assertEquals("graph contain edges from source", graph.sources(v2), source2);
    }

    @Test
    // Covers 
    // remove() size before = n, size after = 1, vertex in graph
    public void testRemoveGraphWithVertex() {
    	Graph<String> graph = emptyInstance();  
    	graph.add(v1);
    	graph.add(v2);
    	graph.set(v1, v2, 10);
    	graph.set(v2, v1, 10);

    	assertEquals("graph contains two vertices", graph.vertices().size(), 2);
    	assertTrue("graph contains an edge from v2", graph.sources(v1).containsKey(v2));
    	assertTrue("graph contains an edge to v2", graph.targets(v1).containsKey(v2));
    	assertTrue("graph include a vertex with the given label", graph.remove(v2));
    	assertEquals("one vertex left", graph.vertices().size(), 1);
    	assertFalse("vertex not found", graph.vertices().contains(v2));
    	assertTrue("no edge in graph", graph.sources(v1).isEmpty());
    	assertTrue("no edge in graph", graph.targets(v1).isEmpty());
    }
    
    @Test
    // Covers 
    // remove() size before = 1, size after = 1, vertex not in graph
    public void testRemoveGraphVertexNotFound() {
    	Graph<String> graph = emptyInstance();  
    	graph.add(v1);
    	assertEquals("graph contains one vertex", graph.vertices().size(), 1);
    	assertFalse("graph does not include a vertex with the given label", graph.remove(v2));
    	assertEquals("graph contains one vertex", graph.vertices().size(), 1);
    	assertTrue("graph contains vertex", graph.vertices().contains(v1));
    }
}
