/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {
    
    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();
    
    // Abstraction function:
    //   represents a graph with distinct labeled vertices and weighted directed edges
    // Representation invariant:
    //   edges: weighted (>0)
    //   vertex label: unique and immutable
    // Safety from rep exposure:
    //   All fields are private and final
    //	 Set guarantees unique vertex labels, Vertex and Edge guarantee immutability
    //	 vertices is a mutable set, so vertices() make defensive copies to avoid aliasing when
    //	 shared to client
    
    // constructor
    /**
     * Create an empty ConcreteVerticesGraph
     */
    public ConcreteEdgesGraph () {
    	checkRep();
    }
    
    // checkRep: check that vertex != null, RI for Edge checked in its own class
    private void checkRep() {
    	assert !vertices.contains(null);
     }
    
    @Override public boolean add(L vertex) {
    	if (vertices.contains(vertex)) {
    		return false;
    	}
    	vertices.add(vertex);
    	checkRep();
    	return true;
    }
    
    @Override public int set(L source, L target, int weight) {
    	int prevWeight = 0;
    	for (Edge<L> edge : new ArrayList<>(edges)) {
    		if (edge.getSource() == source && edge.getTarget() == target) {
    			prevWeight = edge.getWeight();
    			if (weight == 0) {
    				edges.remove(edge);
    			} else {
    				edges.remove(edge);
    				edges.add(new Edge<>(source, target, weight));
    			}
    			checkRep();
    			return prevWeight;
    		}    		
    	}
    	if (weight > 0) {
    		edges.add(new Edge<>(source, target, weight));
    		if (! vertices().contains(source)) {
    			vertices.add(source);
    		}
    		if (! vertices().contains(target)) {
    			vertices.add(target);
    		}
    	}
    	checkRep();
    	return prevWeight;
    }
    
    @Override public boolean remove(L vertex) {
        if (vertices.contains(vertex)) {
        	vertices.remove(vertex);
        	for (Edge<L> edge : new ArrayList<>(edges)) {
        		if (edge.getSource() == vertex || edge.getTarget() == vertex) {
        			edges.remove(edge);
        		}	
        	}
        	checkRep();
        	return true;
        }
        checkRep();
        return false;
    }
    
    @Override public Set<L> vertices() {
        return new HashSet<L>(vertices);
    }
    
    @Override public Map<L, Integer> sources(L target) {
        Map<L, Integer> edgesTo = new HashMap<>();
    	for (Edge<L> edge : edges) {
        	if (edge.getTarget().equals(target)) {
        		edgesTo.put(edge.getSource(), edge.getWeight());
        	}
        }
    	checkRep();
    	return edgesTo;
    }
    
    @Override public Map<L, Integer> targets(L source) {
        Map<L, Integer> edgesFrom = new HashMap<>();
    	for (Edge<L> edge : edges) {
        	if (edge.getSource().equals(source)) {
        		edgesFrom.put(edge.getTarget(), edge.getWeight());
        	}
        }
    	checkRep();
    	return edgesFrom;
    }
    
    // toString()
    /**
     * Return the string representation of the graph, which consists a list of Edge objects,
     * concatenated with "\n" between them (refer to Edge.toString() for string representation
     * of Edge objects)
     * If no Edge object, return an empty string
     */
    public String toString() {
    	
    	String toString = "";
    	for (Edge<L> edge : edges) {
    		toString += edge.toString() + "\n";
    	}
    	return toString.trim();
    }
    
}

/**
 * specification
 *  
 * This class is internal to the rep of ConcreteEdgesGraph. 
 * 
 * This immutable data type represents a directed edge with a source vertex, a target vertex, 
 * and a positive weight of type {@code int}.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge<L> {
    
    // fields
	private final L source;
	private final L target;
	private int weight;
    
    // Abstraction function:
    //   represent a weight direct edge in graph
    // Representation invariant:
    //   weight > 0
    // Safety from rep exposure:
    //   fields are private 
	//	 fields are immutable 
    
    // constructor
    /**
     * Create an Edge
     * @param source generic type vortex label of the source node
     * @param target generic type vortex label of the target node
     * @param weight positive integer weight of the edge
     */
	public Edge (L source, L target,  int weight) {
		this.source = source;
		this.target = target;
		this.weight = weight;
		checkRep();
	}
    
    // checkRep: weight > 0; the rep invariant implicitly require no null values
	private void checkRep() {
		assert source != null;
		assert target != null;
		assert weight > 0;
	}
    
    // methods
    /**
     * @return source label for this edge
     */
	public L getSource() {
		return source;
	}
    
    /**
     * @return target label for this edge
     */
	public L getTarget() {
		return target;
	}
	
    /**
     * @return weight for this edge
     */
	public Integer getWeight() {
		return weight;
	}

    // toString()
	/**
	 * String representation of an edge. The string consist of the source vertex, the arrow 
	 * operator "->", the target vertex, and the positive weight of the edge enclosed in brackets.
	 * In other word, this method returns a string equal to the value of:
	 * 			Edge.getSource() -> Edge.getTarget() (Edge.getWeight())
	 */
	public String toString() {
		return String.format("%s -> %s (%d)", getSource(), getTarget(), getWeight());
	}
    
}
