/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {

	private static final String v1 = "a";
	private static final String v2 = "b";
	private static final String v3 = "c";
	
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph<String>();
    }
    
    /*
     * Testing ConcreteEdgesGraph...
     * 
     * Testing strategy for ConcreteEdgesGraph.toString()
     * num of edges: 0, 1, n
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
    	graph.set(v1, v3, 10);    	
    	String stringRep = "a -> b (5)\na -> c (10)";
    	assertEquals("valid string representation", graph.toString(), stringRep);
    }
    
    
    /*
     * Testing Edge...
     * getSource(v): one test case
     * getTarget(v): one test case
     * getWeight(v): one test case
     * toString(): one test case
     */
    
    // getters
    @Test
    public void testEdgeGetters() {
    	Edge<String> edge = new Edge<>(v1, v2, 5);
    	assertEquals("valid source vertex", edge.getSource(), v1);
    	assertEquals("valid target vertex", edge.getTarget(), v2);
    	assertEquals("valid weight", edge.getWeight(), new Integer(5));  
    }
  
    // toString()
    @Test
    public void testEdgetoString() {
    	Edge<String> edge = new Edge<>(v1, v2, 5);
    	String stringRep = "a -> b (5)";
    	assertEquals("valid string representation", edge.toString(), stringRep);
    }  
}
