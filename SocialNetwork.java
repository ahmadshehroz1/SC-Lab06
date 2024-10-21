package twitter;
import java.util.*;
import java.util.regex.*;
public class SocialNetwork {
    /**
     * Guess follows graph: This method analyzes a list of tweets and builds a social network 
     * where users are linked to those they "follow," inferred from the users they mention in their tweets.
     * 
     * @param tweets a list of tweets, not modified by this method
     * @return a social network (map) where keys are users and values are sets of users they mention in their tweets.
     *         All usernames are converted to lowercase.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        Pattern mentionPattern = Pattern.compile("@(\\w+)");
        for (Tweet tweet : tweets) {
            String author = tweet.getAuthor().toLowerCase(); // Convert author to lowercase
            String text = tweet.getText();  // Get tweet text
            Matcher matcher = mentionPattern.matcher(text);  // Find mentions using regex
            // Initialize the author's mention set if not already present
            followsGraph.putIfAbsent(author, new HashSet<>());
            
            
            // Add all mentions to the author's following set
            while (matcher.find()) {
                String mentionedUser = matcher.group(1).toLowerCase(); // Convert mention to lowercase
                if (!mentionedUser.equals(author)) {  // Prevent self-following
                    followsGraph.get(author).add(mentionedUser);
                }
            }
        }
        return followsGraph;
    }

    
    
    /**
     * Returns a list of people sorted by their influence (total number of followers).
     * 
     * @param followsGraph a social network (map) where keys are users and values are sets of users they follow
     * @return a list of users sorted in decreasing order of their influence
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Integer> followerCount = new HashMap<>();
        // Count the number of followers for each user
        for (Set<String> followedUsers : followsGraph.values()) {
            for (String user : followedUsers) {
                followerCount.put(user, followerCount.getOrDefault(user, 0) + 1);
            }
        }
        // Sort the users by their follower count in descending order
        List<String> influencers = new ArrayList<>(followerCount.keySet());
        influencers.sort((user1, user2) -> followerCount.get(user2) - followerCount.get(user1));

        return influencers;
    }
}