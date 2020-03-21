package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class MySocialNetworkTest {
	 	private static final Instant d1 = Instant.parse("2019-12-29T21:00:00Z");
	    private static final Instant d2 = Instant.parse("2019-12-29T21:00:00Z");
	    private static final Instant d3 = Instant.parse("2019-12-29T21:00:00Z");
	    
	    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much? #rivest", d1);
	    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #RIVEST #hype", d2);
	    private static final Tweet tweet3 = new Tweet(3, "jon", "so syked for new year #hype", d3);
	    
	    @Test(expected=AssertionError.class)
	    public void testAssertionsEnabled() {
	        assert false; // make sure assertions are enabled with VM argument: -ea
	    }
	    
		/*
		 * Testing Strategy: 
		 * 
		 * Partition the inputs as follows:
		 * tweet.size(): 0, 1, > 1
		 * hashtags in tweet: 0, 1, >1
		 * letter case: all uppercase, all lowercase, mix of upper and lower cases
		 * test that the method only returns unique hashtags
	     */
	    
	    @Test
	    // list of tweets: 0
	    public void testGetHashtagNoResult() {
	        Set<String> getHashtags = Extract.getHashtag(Arrays.asList());
	        
	        assertTrue("expected empty set", getHashtags.isEmpty());
	    }
	    
	    @Test
	    // list of tweets: 1
	    // hashtag in tweet: 1
	    public void testGetHashtagOneResult() {
	        Set<String> getHashtags = Extract.getHashtag(Arrays.asList(tweet1));
	        
	        assertEquals("expect one hashtag", getHashtags.size(), 1);
	        assertTrue("set contain rivest", (getHashtags.stream().anyMatch("rivest"::equalsIgnoreCase)
	        		|| getHashtags.stream().anyMatch("#rivest"::equalsIgnoreCase)));
	    }
	    
	    @Test
	    // list of tweets: >1
	    // hashtag in tweet: 1, >1
	    // test that the method is case insensitive
	    // test that the method returns only unique hashtags
	    public void testGetHashtagMultipleResults() {
	        Set<String> getHashtags = Extract.getHashtag(Arrays.asList(tweet1, tweet2));
	        
	        assertEquals("expect two hashtag", getHashtags.size(), 2);
	        assertTrue("set contain rivest", (getHashtags.stream().anyMatch("rivest"::equalsIgnoreCase)
	        		|| getHashtags.stream().anyMatch("#rivest"::equalsIgnoreCase)));
	        assertTrue("set contain hype", (getHashtags.stream().anyMatch("hype"::equalsIgnoreCase)
	        		|| getHashtags.stream().anyMatch("#hype"::equalsIgnoreCase)));
	    }
	    
	    
	    /*
		 * Testing Strategy: 
		 * 
		 * Partition the inputs as follows:
		 * tweet.size(): 0, 1, > 1
		 * number of hashtag: 0, 1, >1
		 * number of common hashtag: 0, 1, >1
		 * if the user does not seem to follow anyone, can either return no key or key with an empty set
		 * should test common hashtag used in tweets of same user (not valid)
	     */
	    
	    @Test
	    // tweet.size(): 1
	    // number of hashtag: 1
	    // number of common hashtag: 0
	    public void testSocialNetworkOneHashtagUnshared() {
	    	Map<String, Set<String>> socialNetwork = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet2));
	        Set<String> expectEmptySet = new HashSet<>();

	        if (socialNetwork.isEmpty()) {
	        	assertTrue("expected empty graph", socialNetwork.isEmpty());
	        } else {
	        	assertEquals("expected one key", socialNetwork.size(), 1);
	        	assertTrue("expected empty set for user", socialNetwork.containsValue(expectEmptySet));
	        }
	    }
	    
	    @Test
	    // tweet.size(): >1
	    // number of hashtag: >1
	    // number of common hashtag: 1
	    public void testSocialNetworkMultipleHashtagsOneShared() {
	    	Map<String, Set<String>> socialNetwork = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2));

	    	assertEquals("expected two keys", socialNetwork.size(), 2);

	        for (String k : socialNetwork.keySet()) {
	        	if (k.toLowerCase() == "alyssa") {
	        		assertEquals("expected a graph with one followed user", socialNetwork.get(k).size(), 1);
	        		assertTrue("the graph include bitdiddle", socialNetwork.get(k).stream().anyMatch("bbitdiddle"::equalsIgnoreCase));
	        	} else if (k.toLowerCase() == "bbitdiddle") {
	        		assertEquals("expected a graph with one followed user", socialNetwork.get(k).size(), 1);
	        		assertTrue("the graph include alyssa", socialNetwork.get(k).stream().anyMatch("alyssa"::equalsIgnoreCase));
	        	} else {
	        		throw new AssertionError ("invalid key");        	
	        	}     		
	        }  
	    }
	    
	    @Test
	    // test common hashtag used in tweets of same user
	    public void testSocialNetworkHashtagsSharedSameUser() {
	    	Map<String, Set<String>> socialNetwork = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet1));
	        Set<String> expectEmptySet = new HashSet<>();

	        if (socialNetwork.isEmpty()) {
	        	assertTrue("expected empty graph", socialNetwork.isEmpty());
	        } else {
	        	assertEquals("expected one key", socialNetwork.size(), 1);
	        	assertTrue("expected empty set for user", socialNetwork.containsValue(expectEmptySet));
	        }
	    }
	    
	    @Test
	    // tweet.size(): >1
	    // number of hashtag: >1
	    // number of common hashtag: >1
	    public void testSocialNetworkMultipleHashtagsMultipleShared() {
	    	Map<String, Set<String>> socialNetwork = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet3));

	    	assertEquals("expected three keys", socialNetwork.size(), 3);

	        for (String k : socialNetwork.keySet()) {
	        	if (k.toLowerCase() == "alyssa") {
	        		assertEquals("expected a graph with one followed user", socialNetwork.get(k).size(), 1);
	        		assertTrue("the graph include bitdiddle", socialNetwork.get(k).stream().anyMatch("bbitdiddle"::equalsIgnoreCase));
	        	} else if (k.toLowerCase() == "bbitdiddle") {
	        		assertEquals("expected a graph with two followed users", socialNetwork.get(k).size(), 2);
	        		assertTrue("the graph include alyssa", socialNetwork.get(k).stream().anyMatch("alyssa"::equalsIgnoreCase));
	        		assertTrue("the graph include alyssa", socialNetwork.get(k).stream().anyMatch("jon"::equalsIgnoreCase));
	        	}else if (k.toLowerCase() == "jon") {
	        		assertEquals("expected a graph with one followed user", socialNetwork.get(k).size(), 1);
	        		assertTrue("the graph include alyssa", socialNetwork.get(k).stream().anyMatch("bbitdiddle"::equalsIgnoreCase));
	        	} else {
	        		throw new AssertionError ("invalid key");        	
	        	}     		
	        }  
	    }
	    
}
