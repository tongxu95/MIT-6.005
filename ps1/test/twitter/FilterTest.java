/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2019-12-24T20:00:00Z");
    private static final Instant d4 = Instant.parse("2019-12-24T20:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "jen", "THERE ARE SO MUCH ONLINE RESOURCES TO LEARN PROGRAMMING @EDX @OCW", d3);
    private static final Tweet tweet4 = new Tweet(4, "jen", "I studied at the University of Calgary", d4);


    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    /*
	 * Testing Strategy: 
	 * 
	 * Partition the inputs as follows:
	 * tweet.size(): 0, 1, > 1
	 * number of tweets written by user: 0, 1, >1
	 * username case: capital and/or lower case
	 * if number of tweets written by user >1, should be in the same order as in the input list
     */
    
    @Test
    // tweet size: 1
    // number of tweets written by the user: 0
    public void testWrittenByOneTweetsZeroResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1), "bbitdiddle");

        assertTrue("expected empty list", writtenBy.isEmpty());
    }
    
    @Test
    // tweet size: >1
    // number of tweets written by the user: 1
    // test case sensitivity
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "ALYSSA");
        
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }
    
    @Test
    // tweet size: >1
    // number of tweets written by the user: >1
    // test that the list of tweets returned is in the same order as the input list
    public void testWrittenByMultipleTweetsMultipleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet3, tweet4), "jen");
        
        assertEquals("expected list of two tweet", 2, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.containsAll(Arrays.asList(tweet3, tweet4)));
        assertEquals("expected same order", 0, writtenBy.indexOf(tweet3));
    }

    
    /*
	 * Testing Strategy: 
	 * 
	 * Partition the inputs as follows:
	 * tweet.size(): 0, 1, > 1
	 * number of tweets within timespan: 0, 1, >1
	 * if number of tweets within timespan >1, should be in the same order as in the input list
     */
    
    @Test
    // tweet.size(): >1
    // number of tweet within timespan: >1
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }
    
    @Test
    // tweet.size(): 1
    // number of tweet within timespan: 0
    public void testInTimespanOneTweetZeroResult() {
        Instant testStart = Instant.parse("2018-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2018-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1), new Timespan(testStart, testEnd));
        
        assertTrue("expected an empty list", inTimespan.isEmpty());
    }    

    
    /*
	 * Testing Strategy: 
	 * 
	 * Partition the inputs as follows:
	 * tweet.size(): 0, 1, > 1
	 * number of words: 0, 1, >1
	 * tweets containing words: 0, 1, >1
	 * ensure that the test is insensitive to letter case
	 * test word boundary: ie. if the word is "the", should not match "there"
	 * if number of tweets containing words >1, should be in the same order as in the input list
     */
    
    @Test
    // tweet.size(): >1
    // number of words: 1
    // tweets containing words: >1
    // test case sensitivity of method
    // test that the method returns tweet in the same order as the input list
    public void testContainingOneWordMultipleResults() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("TALK"));
        
        assertEquals("expected list of two tweets", containing.size(), 2);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }
    
    @Test
    // tweet.size(): >1
    // number of words: >1
    // tweets containing words: >1
    // test case sensitivity of method
    // test word boundary: ie. if the word is "the", should not match "there"
    // test that the method returns tweet in the same order as the input list
    public void testContainingMultipleWordsMultipleResults() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet2, tweet3, tweet4), Arrays.asList("the", "rivest"));
        
        assertEquals("expected non-empty list", containing.size(), 2);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet2, tweet4)));
        assertEquals("expected same order", 0, containing.indexOf(tweet2));
    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */

}
