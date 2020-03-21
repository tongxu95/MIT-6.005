 /* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.junit.Test;

public class SocialNetworkTest {

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2019-12-27T22:00:00Z");
    private static final Instant d3 = Instant.parse("2019-12-27T22:00:00Z");

    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "jen", "I have learned a lot from mit's course material @OCW", d2);
    private static final Tweet tweet3 = new Tweet(3, "jen", "I am learning to program through @EDX @OCW @JEN", d3);
    
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /*
	 * Testing Strategy: 
	 * 
	 * Partition the inputs as follows:
	 * tweet.size(): 0, 1, > 1
	 * username-mention in a tweet: 0, 1, >1 (all unique, some unique all duplicates)
	 * username letter case: may be capital and/or lower case
	 * if the user does not seem to follow anyone, can either return no key or key with an empty set
	 * should test that the social network for a given user does not include themselves  
	 * should test to ensure the all the keys and the usernames included in the set are unique
	 * 
     */
    @Test
    // tweet.size(): 0
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    // tweet.size(): 1
    // username-mention in a tweet: 0
    public void testGuessFollowsOneTweetGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1));
        Set<String> expectEmptySet = new HashSet<>();
        
        // return either an empty map or a key with empty set
        if (followsGraph.isEmpty()) {
        	assertTrue("expected empty graph", followsGraph.isEmpty());
        } else {
        	assertEquals("expected one key", followsGraph.size(), 1);
        	assertTrue("expected empty set for user", followsGraph.containsValue(expectEmptySet));
        }
        
    }   	
    
    @Test
    // tweet.size(): >1
    // username-mention in a tweet: >1
    //
    // test that the social network returns unique usernames in set even if the same username
    // is mentioned in multiple tweets 
    // 
    // test that the social network does not include the given user
    public void testGuessFollowsMultipleTweetsOneGraph() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet2, tweet3));
        
        assertEquals("expected one key", followsGraph.size(), 1);

        for (String k : followsGraph.keySet()) {
        	if (k.toLowerCase() == "jen") {
        		assertEquals("expected a graph with two followed users", followsGraph.get(k).size(), 2);
        		assertTrue("the graph include user", followsGraph.get(k).stream().anyMatch("OCW"::equalsIgnoreCase));
        		assertTrue("the graph include user", followsGraph.get(k).stream().anyMatch("EDX"::equalsIgnoreCase));
        	} else {
        		throw new AssertionError ("invalid key");        	
        	}     		
        }
    } 
    
    /*
	 * Testing Strategy: 
	 * 
	 * Partition the inputs as follows:
	 * followGraph.keySet(): 0, 1, > 1
	 * followGraph.values(): 0, 1, >1 
	 * influencer impact: 0, 1, >1
	 * test to ensure that the list influencers are sorted in descending order and includes all
	 * unique usernames in followGraph
     */
    
    @Test
    // followGraph.keySet(): 0
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }

    @Test
    // followGraph.keySet(): 1
    // followGraph.values(): 1
    // influencer impact: 0, 1
    public void testInfluencersOneEntryFollowsGraph() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        Set<String> alyssaFollowing = new HashSet<>(Arrays.asList("OCW"));
        followsGraph.put("alyssa", alyssaFollowing);
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertEquals("expected two usernames returned", influencers.size(), 2);
        assertEquals("expected OCW to be at the first index", influencers.get(0), "OCW");
        assertEquals("expected alyssa to be at the second index", influencers.get(1), "alyssa");
    }
    
    @Test
    // followGraph.keySet(): >1
    // followGraph.values(): >1
    // influencer impact: 0, 1, >1
    public void testInfluencersMultipleEntrysFollowsGraph() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        Set<String> jenFollowing = new HashSet<>(Arrays.asList("OCW", "EDX"));
        followsGraph.put("jen", jenFollowing);
        Set<String> alyssaFollowing = new HashSet<>(Arrays.asList("OCW"));
        followsGraph.put("alyssa", alyssaFollowing);
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertEquals("expected four usernames returned", influencers.size(), 4);
        assertEquals("expected OCW to be at the first index", influencers.get(0), "OCW");
        assertEquals("expected EDX to be at the second index", influencers.get(1), "EDX");
    }
    
    
    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */

}
