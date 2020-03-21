/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {

	private static final String v1 = "a";
	private static final String v2 = "b";
	private static final String v3 = "c";
	
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<String>();
    }
    
    /*
     * Testing ConcreteVerticesGraph...
     *   
     * Testing strategy for ConcreteVerticesGraph.toString()
     * num of edges: 0, 1, n
     * 
     */
    // num of edges = 0
    @Test
    public void testGraphToStringEmpty() {
    	Graph<String> graph = emptyInstance();  
    	assertTrue("empty string", graph.toString().isEmpty());
    }
    
    // num of edges = 1
    @Test
    public void testGraphToStringOneEdge() {
    	Graph<String> graph = emptyInstance();  
    	graph.set(v1, v2, 5);
    	String stringRep = "a -> b (5)";
    	assertEquals("valid string representation", graph.toString(), stringRep);
    }
    
    // num of edges = n
    @Test
    public void testGraphToStringMultipleEdges() {
    	Graph<String> graph = emptyInstance();  
    	graph.set(v1, v2, 5);
    	graph.set(v2, v3, 10);  
    	String stringRep = "a -> b (5)\nb -> c (10)";
    	assertEquals("valid string representation", graph.toString(), stringRep);
    }
    
    // num of edges = n
    @Test
    public void testGraphToStringMultipleEdges2() {
    	Graph<String> graph = emptyInstance();  
    	graph.set(v1, v2, 5);
    	graph.set(v1, v3, 10);  
    	graph.set(v2, v3, 10);  
    	String stringRep = "a -> b (5)\na -> c (10)\nb -> c (10)";
    	assertEquals("valid string representation", graph.toString(), stringRep);
    }

    /*
     * Testing Vertex...
     * 
     * Testing strategy for Vertex
     * getVertex(): one test case
     * getTarget(): size = 0, 1, n
     * setEdge(v,w)
     * 	size before = 0, 1, n
	 * 	size after = 0, 1, n
	 * 	v present of not (w changed or not)
	 * 	w = 0, >0
     * toString(): size = 0, 1, n edges
     */
    
    @Test
    public void testVertexGetVertex() {
    	Vertex<String> vertex = new Vertex<>(v1);
    	String label = "a";
    	assertEquals(vertex.getVertex(), label);
    	assertTrue(vertex.getTargets().isEmpty());
    }
    
    // SetEdge(v,w): size before: 0, size after: 1
    // getTargets(): size = 1
    @Test
    public void testVertexSetEdgeAddToEmptyMap() {
    	Vertex<String> vertex = new Vertex<>(v1);
    	vertex.setEdge(v2, 5);
    	Map<String, Integer> edge = new HashMap<>();
    	edge.put(v2, 5);
    	assertEquals(vertex.getTargets(), edge);
    }
    
    // SetEdge(v,w): size before: 1, size after: 0
    // getTargets(): size = 0
    @Test
    public void testVertexSetEdgeRemoveEdge() {
    	Vertex<String> vertex = new Vertex<>(v1);
    	vertex.setEdge(v2, 5);
    	assertEquals("vertex contained edge", vertex.setEdge(v2, 0), 5);
    	assertTrue("edge removed", vertex.getTargets().isEmpty());
    }
    
    // SetEdge(v,w): size before: 0, size after: 0
    @Test
    public void testVertexSetEdgeNoChange() {
    	Vertex<String> vertex = new Vertex<>(v1);
    	vertex.setEdge(v2, 0);
    	assertTrue("vertex contain no edge", vertex.getTargets().isEmpty());
    }
    
    // SetEdge(v,w): size before: 1, size after: n
    // getTargets(): size = n
    @Test
    public void testVertexSetEdgeAddEdge() {
    	Vertex<String> vertex = new Vertex<>(v1);
    	vertex.setEdge(v2, 5);
    	vertex.setEdge(v3, 10);
    	Map<String, Integer> edge = new HashMap<>();
    	edge.put(v2, 5);
    	edge.put(v3, 10);
    	assertEquals(vertex.getTargets(), edge);
    }
    
    // SetEdge(v,w): size before: 1, size after: 1
    @Test
    public void testVertexSetEdgeChangeEdge() {
    	Vertex<String> vertex = new Vertex<>(v1);
    	vertex.setEdge(v2, 5);
    	Map<String, Integer> edge = new HashMap<>();
    	edge.put(v2, 5);
    	assertEquals(vertex.getTargets(), edge);
    	
    	vertex.setEdge(v2, 10);
    	Map<String, Integer> edge2 = new HashMap<>();
    	edge2.put(v2, 10);
    	assertEquals(vertex.getTargets(), edge2);
    }
  
    // toString(): size = 1
    @Test
    public void testVertextoStringOneEdge() {
    	Vertex<String> vertex = new Vertex<>(v1);
    	vertex.setEdge(v2, 5);
    	String stringRep = "a -> b (5)";
    	assertEquals("valid string representation", vertex.toString(), stringRep);
    }  
    
    // toString(): size = n
    @Test
    public void testVertextoStringMultipleEdges() {
    	Vertex<String> vertex = new Vertex<>(v1);
    	vertex.setEdge(v2, 5);
    	vertex.setEdge(v3, 10);
    	String stringRep = "a -> b (5)\na -> c (10)";
    	assertEquals("valid string representation", vertex.toString(), stringRep);
    }
    
}
