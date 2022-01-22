package vendor.app.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import vendor.app.User;

public abstract class JsonManager {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static ObjectNode addressList = mapper.createObjectNode();
    private static ObjectNode tweetsList = mapper.createObjectNode();
    private static int addressId = 0;
    private static int tweetId = 0;

    protected static void addToAddressList(ObjectNode content) {
        addressList.set(String.valueOf(addressId++), content);
    }

    protected static void addToTweetsList(ObjectNode content) {
        ObjectNode tweet = mapper.createObjectNode();
        tweet.put("id", ++tweetId);
        tweet.set("tweet", content);
        tweetsList.set(String.valueOf(tweetId), tweet);
    }

    protected static void removeTweet(int tweetId) {
        tweetsList.remove(String.valueOf(tweetId));
    }

    protected static void removeUserTweet(int tweetId, int userId) {
        for (JsonNode node : tweetsList) {
            if ((node.get("id").asInt() == tweetId) && (node.findValue("userId").asInt() == userId)) {
                tweetsList.remove(String.valueOf(tweetId));
                return;
            }
        }
    }

    public static boolean searchAddress(int userId) {
        for (JsonNode node : addressList) {
            if (node.get("userId").asInt() == userId) {
                System.out.println("You already set an address.");
                System.out.println("Your address is: " + node.get("address").asText());

                return true;
            }
        }

        return false;
    }

    public static boolean searchTweets(int userId) {
        if (tweetsList.size() == 0) return false;
        for(JsonNode node : tweetsList) {
            if (node.findValue("userId").asInt() == userId) {
                System.out.println("\nTweet ID: " + node.get("id"));
                System.out.println("From: " + User.registeredUsers.get(node.findValue("userId").asInt()).getEmail());
                System.out.println(node.findValue("tweetContent").asText());
                System.out.println("- " + node.findValue("date").asText());
            }
        }

        return true;
    }

    public static boolean getTweets() {
        if (tweetsList.size() == 0) return false;
        for (JsonNode node : tweetsList) {
            System.out.println("\nFrom: " + User.registeredUsers.get(node.findValue("userId").asInt()).getEmail());
            System.out.println("Tweet ID: " + node.get("id"));
            System.out.println(node.get("tweet").get("tweetContent").asText());
            System.out.println("- " + node.get("tweet").get("date").asText());
        }

        return true;
    }
}
