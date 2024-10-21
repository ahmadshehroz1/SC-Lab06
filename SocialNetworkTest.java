package twitter;

import org.junit.Test;
import java.time.Instant;
import java.util.*;

import static org.junit.Assert.*;

public class SocialNetworkTest {

    @Test
    public void emptyTweets() {
        List<Tweet> tweets = new ArrayList<>();
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(graph.isEmpty());
    }

    @Test
    public void noMentions() {
        List<Tweet> tweets = Arrays.asList(new Tweet(1, "user1", "Hello world!", Instant.now()));
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(graph.get("user1").isEmpty());
    }

    @Test
    public void singleMention() {
        List<Tweet> tweets = Arrays.asList(new Tweet(1, "user1", "Hello @user2!", Instant.now()));
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(graph.get("user1").contains("user2"));
    }

    @Test
    public void multipleMentions() {
        List<Tweet> tweets = Arrays.asList(new Tweet(1, "user1", "Hi @user2 and @user3!", Instant.now()));
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(graph.get("user1").containsAll(Arrays.asList("user2", "user3")));
    }

    @Test
    public void multipleTweetsFromOneUser() {
        List<Tweet> tweets = Arrays.asList(
            new Tweet(1, "user1", "Hi @user2!", Instant.now()),
            new Tweet(2, "user1", "Hello @user3!", Instant.now())
        );
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(graph.get("user1").containsAll(Arrays.asList("user2", "user3")));
    }

    @Test
    public void emptyGraph() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue(influencers.isEmpty());
    }

    @Test
    public void singleUserNoFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("user1", new HashSet<>());
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue(influencers.isEmpty());
    }

    @Test
    public void singleInfluencer() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("user1", new HashSet<>(Arrays.asList("user2")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals(Arrays.asList("user2"), influencers);
    }

    @Test
    public void multipleInfluencers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("user1", new HashSet<>(Arrays.asList("user2", "user3")));
        followsGraph.put("user2", new HashSet<>(Arrays.asList("user3")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals(Arrays.asList("user3", "user2"), influencers);
    }

    @Test
    public void tiedInfluence() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("user1", new HashSet<>(Arrays.asList("user2")));
        followsGraph.put("user3", new HashSet<>(Arrays.asList("user2")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals(Arrays.asList("user2"), influencers);
    }
}
