/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph<L> implements Graph<L> {
    
    private final List<Vertex<L>> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   represents a graph with distinct labeled vertices and weighted directed edges
    // Representation invariant:
    //   edges: weighted (>0)
    //   vertex label: unique and immutable
    // Safety from rep exposure:
    //   all fields are private and final
    //	 vertices is a mutable list, so vertices() make defensive copies to avoid aliasing when
    //	 shared to client
    
    // constructor
    /**
     * Create an empty ConcreteVerticesGraph
     */
    public ConcreteVerticesGraph() {
    	checkRep();
    }
    
    // checkRep: unique vertices
    public void checkRep() {
    	Set<Vertex<L>> verticesSet = new HashSet<>(vertices);
    	assert verticesSet.size() == vertices.size();
    }
    
    @Override public boolean add(L label) {
        boolean labelAdded = true;
    	for (Vertex<L> vertex : vertices) {
        	if (vertex.getVertex() == label) {
        		labelAdded = false;
        		break;
        	}
        }
    	if (labelAdded) {
    		vertices.add(new Vertex<>(label));
    	}
        checkRep();
        return labelAdded;
    }
    
    @Override public int set(L source, L target, int weight) {
        int prevWeight = 0;
        boolean containsSourceVertex = false;
        boolean containsTargetVertex = false;
        for (Vertex<L> vertex : vertices) {
        	if (vertex.getVertex() == source) {
        		prevWeight = vertex.setEdge(target, weight);
        		containsSourceVertex = true;
        	} else if (vertex.getVertex() == target) {
        		containsTargetVertex = true;
        	}
        }
        if (! containsSourceVertex && weight != 0) {
        	Vertex<L> sourceVertex = new Vertex<> (source);
        	sourceVertex.setEdge(target, weight);
        	vertices.add(sourceVertex);
        }
        if (! containsTargetVertex && weight != 0) {
            vertices.add(new Vertex<> (target));
        }
        checkRep();
        return prevWeight;        
    }
    
    @Override public boolean remove(L label) {
        boolean containsLabel = false;
    	for (Vertex<L> vertex : new ArrayList<>(vertices)) {
        	if (vertex.getVertex() == label) {
        		containsLabel = vertices.remove(vertex);
        	} else {
        		vertex.setEdge(label, 0);
        	}	
        }
    	return containsLabel;
    }
    
    @Override public Set<L> vertices() {
        Set<L> labels = new HashSet<>();
    	for (Vertex<L> vertex : vertices) {
        	labels.add(vertex.getVertex());
        }
    	return labels;
    }
    
    @Override public Map<L, Integer> sources(L target) {
    	Map<L, Integer> sources = new HashMap<>();
    	for (Vertex<L> vertex : vertices) {
    		if (vertex.getVertex() != target && vertex.getTargets().containsKey(target)) {
    			sources.put(vertex.getVertex(), vertex.getTargets().get(target));    			
    		}
    	}
    	return sources;
    }
    
    @Override public Map<L, Integer> targets(L source) {
        Map<L, Integer> targets = new HashMap<>();
    	for (Vertex<L> vertex : vertices) {
        	if (vertex.getVertex().equals(source)) {
        		targets = vertex.getTargets();
        		break;
        	}
        }
    	return targets;
    }
    
    // toString()
	/**

	 * Return the string representation the graph, which consists a list of Vertex objects,
     * concatenated with "\n" between them (refer to Vertex.toString() for string representation
     * of Vertex objects)
     * If no Vortex object, return an empty string
	 */
    public String toString() {
    	String toString = "";
    	for (Vertex<L> vertex : vertices) {
    		toString += vertex.toString() + "\n";
    	}
    	return toString.trim();
    }
    
}

/**
 * specification
 * 
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * This mutable data type represent a source vertex in graph, and stores all edges from
 * (target vertex and a positive weight of type {@code int}) and edges to (source vertex and 
 * a positive weight of type {@code int}) this vertex. 
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex<L> {
    
    // fields
    private final L vertex;
    private final Map<L, Integer> edgesFrom = new HashMap<>();
    
    // Abstraction function:
    //   Represent a source vertex in the graph and map all edges from this vertex 
    //   (key: target vertex, value: positive weight of the edge)
    // Representation invariant:
    //   weight > 0
    //	 vertex label: unique and immutable
    // Safety from rep exposure:
    //   fields are private and final
    //   vertex is immutable
    //	 map guarantee unique keys (vertex labels)
    //   edgesFrom is a mutable map; so getTargets() make defensive copies 
    //   to avoid aliasing when shared to the client    
    
    // constructor
    /**
     * Create a Vertex
     * @param vertex a generic type vertex label
     */
    public Vertex (L vertex) {
    	this.vertex = vertex;
    	checkRep();
    }
    
    // checkRep: weight > 0; the rep invariant implicitly require no null values
    private void checkRep() {
    	assert vertex != null;
    	if (! edgesFrom.isEmpty())
    		for (L target : edgesFrom.keySet()) {
    			assert target != null;
    			assert edgesFrom.get(target) > 0;
    		}
    }
    
    // methods
    /**
     * @return label for this Vertex
     */
    public L getVertex() {
    	return vertex;
    }
    
    /**
     * @return the target labels and the weight from this vertex label to the target labels
     * as keys and values in a map  
     */
    public Map<L, Integer> getTargets() {
    	return new HashMap<L, Integer> (edgesFrom);
    }
    
    /**
     * Add, change, or remove a weighted directed edge from this vertex.
     * If weight is nonzero, add an edge or update the weight of that edge;
     * If weight is zero, remove the edge if it exists (the vertex is not
     * otherwise modified).
     * 
     * @param label of the vertex
     * @param weight nonnegative weight of the edge
     * @return the previous weight of the edge, or zero if there was no such
     *         edge
     */
    public int setEdge (L target, int weight) {
    	int prevWeight = 0;
    	if (edgesFrom.containsKey(target)) {    		
    		if (weight != 0) {
        		prevWeight = edgesFrom.put(target, weight);
    		} else {
    			prevWeight = edgesFrom.remove(target);
    		}
    	} else {
    		if (weight > 0) {
    			edgesFrom.put(target, weight);
    		}
    	} 
    	checkRep();
    	return prevWeight;
    }
    
    // toString()
	/**
	 * String representation of an vertex. The string consist of the source vertex, the arrow 
	 * operator "->", the target vertex, and the positive weight of the edge enclosed in brackets.
	 * In other word, this method returns a string equal to the value of:
	 * 			source -> target (weight)
	 */
    public String toString() {
    	String toString = "";
    	for (L target : edgesFrom.keySet()) {
    		toString += String.format("%s -> %s (%d)\n", getVertex(), target, 
    				edgesFrom.get(target));
    	}
    	return toString.trim();    	
    }
    
}
