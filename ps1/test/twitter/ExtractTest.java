/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;


import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2019-12-24T20:00:00Z");
    private static final Instant d4 = Instant.parse("2019-12-24T20:00:00Z");
    private static final Instant d5 = Instant.parse("2019-12-26T20:00:00Z");
    private static final Instant d6 = Instant.parse("2019-12-26T20:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "jen", "THERE ARE SO MUCH ONLINE RESOURCES TO LEARN PROGRAMMING @EDX @OCW", d3);
    private static final Tweet tweet4 = new Tweet(4, "jen", "jen@gmail.com is not a valid username", d4);
    private static final Tweet tweet5 = new Tweet(5, "jen", "$$@jen is a valid username-mention", d5);
    private static final Tweet tweet6 = new Tweet(6, "jen", "@JEN is a valid username-mention", d6);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    /*
	 * Testing Strategy: 
	 * 
	 * Partition the inputs as follows:
	 * tweet.size(): 0, 1, > 1
	 * sorting: tweets sorted by timespan, not sorted
	 * tweet timespan: same, unique
	 * 
	 * Ensure list of tweets are not altered by the method
	 * 
     */
    
    @Test
    // covers tweets.size() > 1
    // sorted list of tweets
    public void testGetTimespanSortedTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test
    // covers tweets.size() > 1
    // unsorted list of tweets
    public void testGetTimespanUnsortedTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet1));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test
    // covers tweets.size() == 1
    public void testGetTimespanOneTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
    }
    
    @Test
    // covers tweets.size() == 0
    public void testGetTimespanNoTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList());
        
        assertEquals("expected start and end", timespan.getStart(), timespan.getEnd());
    }

    @Test
    // ensure argument (list of tweets) are not altered
    public void testTweetsUnaltered() {
        List<Tweet> tweets = Arrays.asList(tweet2, tweet1);
    	Extract.getTimespan(tweets);
        
        assertEquals("list of tweets unaltered", tweets, Arrays.asList(tweet2, tweet1));
    }
    
    /*
	 * Testing Strategy: 
	 * 
	 * Partition the inputs as follows:
	 * tweet.size(): 0, 1, >1
	 * users mentioned in a tweet: 0, 1, > 1
	 * letter case: all uppercase, all lowercase, mix of upper and lower cases
	 * embedded usernames: the username-mention is proceeded and/or followed by valid username characters / invalid username characters
	 * mentioned users: all unique, some unique all duplicates 
     */
    
    @Test
    // list of tweets: size of 1
    // tweet mentions 0 user
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test
    // list of tweets: size of 1
    // tweet mentions 2 unique usernames
    public void testGetMentionedUsersMultipleMentions() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3));
        
        assertEquals("expected set of two usernames", mentionedUsers.size(), 2);
        assertTrue("set contains EDX", mentionedUsers.stream().anyMatch("EDX"::equalsIgnoreCase));
        assertTrue("set contains OCW", mentionedUsers.stream().anyMatch("OCW"::equalsIgnoreCase));
    }
    
    @Test
    // list of tweets: size of 1
    // embedded usernames: invalid username-mention
    public void testGetMentionedUsersInvalidMentions() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test
    // list of tweets: >1 
    // contain duplicative username-mention (test case sensitivity)
    // embedded usernames: valid username-mention
    public void testGetMentionedUsersDuplicativeAndEmbeddedMentions() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet5, tweet6));
        
        assertEquals("expected set of one username", mentionedUsers.size(), 1);
        assertTrue("set contains jen", mentionedUsers.stream().anyMatch("jen"::equalsIgnoreCase)); 
    }
    
    
    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}
