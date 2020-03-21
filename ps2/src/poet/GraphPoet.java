/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.*;
import java.util.*;

import graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();
    
    // Abstraction function:
    //   represent a word affinity graph where vertices in the graph are words and the edges
    //   count adjaciences (the weight counts the number of times "w1" is followed by "w2" in 
    //   the corpus; used as poetry generator
    // Representation invariant:
    //   vertices: non-empty, case insensitive strings of non-space non-newline characters
    //   rep invariant for graph 
    // Safety from rep exposure:
    //   graph is private and final
    
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
    	Scanner s = new Scanner(new BufferedReader(new FileReader(corpus)));
    	s.useDelimiter("\\s+");
    	String source = s.next().toLowerCase();
    	while (s.hasNext()) {  
    		String target = s.next().toLowerCase();
    		if (! graph.add(source)) {
    			Map<String, Integer> targets = graph.targets(source);
    			if (! targets.containsKey(target)) {
    				graph.set(source, target, 1);
    			} else {
    				int prevWeight = targets.get(target);
    				graph.set(source, target, (prevWeight + 1));
    			}
    		} else {
    			graph.set(source, target, 1);
    		}
    		source = target;
    	}
    	s.close();
    	checkRep();
    }
    
    // checkRep
    private void checkRep() {
    	for (String vertex: graph.vertices()) {
    		assert (! vertex.contains(" ")) && (! vertex.contains("\n"));
    	}
    }
    
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        String[] wordsInInput = input.split("\\s+");
        String poem = "";
        for (int i = 0; i < (wordsInInput.length - 1); i++) {
        	poem += wordsInInput[i] + " ";
        	String bridgeWord = "";
            int maxWeight = 0;
            int weightTwoEdges = 0;
        	String sourceWord = wordsInInput[i].toLowerCase();
        	String targetWord = wordsInInput[i+1].toLowerCase();
        	Map<String, Integer> wordsFromSource = graph.targets(sourceWord);
        	Map<String, Integer> wordsToTarget = graph.sources(targetWord);
        	for (String word : wordsFromSource.keySet()) {
        		if (wordsToTarget.containsKey(word)) {
        			weightTwoEdges = wordsFromSource.get(word) + wordsToTarget.get(word);
        			if(weightTwoEdges > maxWeight) {
        				bridgeWord = word;
        				maxWeight = weightTwoEdges;
        			}
        		}
        	}
        	if (! bridgeWord.isEmpty()) {        		
        		poem += bridgeWord + " ";
        	}
        }
        poem += wordsInInput[wordsInInput.length - 1];
        return poem;	
    }
    
    // toString()
    /**
     * Return the string representation the graph
     */
    @Override public String toString() {
    	return graph.toString();
    }
    
}
