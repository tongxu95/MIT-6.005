/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.*;





/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {
    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {

        if (tweets.isEmpty()) {
        	return new Timespan (Instant.now(), Instant.now());
        } else {
        	List<Tweet> sortedTweets = new ArrayList<> (tweets);
        	sortedTweets.sort(Comparator.comparing(Tweet::getTimestamp));
        	return new Timespan(sortedTweets.get(0).getTimestamp(), 
        			sortedTweets.get(sortedTweets.size() - 1).getTimestamp());
        }
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {

        Set<String> mentionedUsernames = new HashSet<>();
        for (Tweet t : tweets) {
        	//regular expression for valid username-mention
            Pattern pattern = Pattern.compile("(?<=[^a-zA-Z0-9_-]+@)[A-Za-z0-9_-]+(?=[^a-zA-Z0-9_-]*)");
            Matcher match = pattern.matcher(t.getText());
            //add unique usernames to the set
            while (match.find()) {
            	String username = match.group().toLowerCase();
            	if (! mentionedUsernames.contains(username)) {
            		mentionedUsernames.add(username);
            	}
            }
        }
        return mentionedUsernames;
    }  
    
    /**
     * Get hashtag in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of hashtag in the text of the tweets.
     *         A hashtag is "#" followed by alphanumeric (assume same rule as username)
     */
    public static Set<String> getHashtag(List<Tweet> tweets) {
        Set<String> hashtags = new HashSet<>();
        for (Tweet t : tweets) {
        	//regular expression for valid username-mention
            Pattern pattern = Pattern.compile("(?<=#)[A-Za-z0-9_-]+(?=[^a-zA-Z0-9_-]*)");
            Matcher match = pattern.matcher(t.getText());
            //add unique hashtag to the set
            while (match.find()) {
            	String hashtag = match.group().toLowerCase();
            	if (! hashtags.contains(hashtag)) {
            		hashtags.add(hashtag);
            	}
            }
        }
        return hashtags;
    }  
}