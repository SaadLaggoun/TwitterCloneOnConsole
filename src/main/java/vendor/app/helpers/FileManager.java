package vendor.app.helpers;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class FileManager extends JsonManager {

    public static void create(String listName, ObjectNode content) {
        if (listName.equals("addressList")) {
            addToAddressList(content);
        } else if (listName.equals("tweetsList")) {
            addToTweetsList(content);
        }
    }

    public static void delete(int elementId) {
        removeTweet(elementId);
    }

    public static void delete(int elementId, int userId) {
        removeUserTweet(elementId, userId);
    }
}
