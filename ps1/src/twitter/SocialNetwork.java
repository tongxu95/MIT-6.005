/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;


import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;



/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion.
     *         All the Twitter usernames in the returned social network must be
     *         either authors or @-mentions in the list of tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {

    	Map <String, Set<String>> socialNetwork = new HashMap <>();
		Map <String, Set<String>> commonHashtagsMap = new HashMap <>();
		
    	for (Tweet t : tweets) {
    		String user = t.getAuthor().toLowerCase();
    		Set <String> guessFollowedUsers = Extract.getMentionedUsers(Arrays.asList(t));
    		
    		//remove self-mention
    		if (guessFollowedUsers.contains(user)) {
    			guessFollowedUsers.remove(user);
    		}
    		//build social network using username-mention
    		if (! socialNetwork.containsKey(user)) {
    			socialNetwork.put(user, guessFollowedUsers);
    		} else {
    			//avoid repeated mentions
    			for (String followedUser : guessFollowedUsers) {
    				if (! socialNetwork.get(user).contains(followedUser)) {
    					socialNetwork.get(user).add(followedUser);
    				}
    			}
    		}
    		
    		// map hashtag to user who used the same hashtags 
    		Set <String> extractHashtags = Extract.getHashtag(Arrays.asList(t));
    		Set <String> commonHashtags = new HashSet <>();
    		for (String hashtag : extractHashtags) {
    			if (! commonHashtagsMap.containsKey(hashtag)) {
    				commonHashtags.add(user);
    				commonHashtagsMap.put(hashtag, commonHashtags);
    			} else {
    				//avoid repeat values (a user may mention the same hashtag in multiple tweets)
    				if (! commonHashtagsMap.get(hashtag).contains(user)) {    					
        				commonHashtagsMap.get(hashtag).add(user);
    				}
    			}
    		}
    		
    	}
    	
    	// add to social network using common hashtag
    	for (String commonHashtag: commonHashtagsMap.keySet()) {
    		Set <String> usersSameHashtag = commonHashtagsMap.get(commonHashtag);
    		for (String username : usersSameHashtag) {
				Set <String> followingUsers = new HashSet <>(usersSameHashtag);
				followingUsers.remove(username);
				for (String followingUser: followingUsers) {
					if (! socialNetwork.get(username).contains(followingUser)) {
						socialNetwork.get(username).add(followingUser);
					}
				}
			}
    	}
    	return socialNetwork;
    }

    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
    	Map <String, Integer> influencers = new HashMap <>();
    	for (String user : followsGraph.keySet()) {
    		if (! influencers.containsKey(user)) {
    			influencers.put(user, 0);
    		}
    		for (String following : followsGraph.get(user)) {
    			if (! influencers.containsKey(following)) {
    				influencers.put(following, 1);
    			} else {
    				influencers.put(following, influencers.get(following) + 1);
    			}			
    		}
    	}
    	
    	List <String> influencersSorted = new ArrayList <>();
    	while (! influencers.isEmpty()) {
    		int greatestFollowing = Collections.max(influencers.values());
    		for (String influencer : influencers.keySet()) {
    			if (influencers.get(influencer) == greatestFollowing) {
    				influencersSorted.add(influencer);
    				influencers.remove(influencer);
    				break;
    			}
    		}
    	}
    	return influencersSorted;    	
    }

}
