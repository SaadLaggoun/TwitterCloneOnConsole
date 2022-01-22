package vendor.app.views;

import vendor.app.Router;
import vendor.app.User;
import vendor.app.controllers.ViewsController;
import vendor.app.helpers.FileManager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Views extends Router{
    public static List<String> viewsList = new ArrayList<>();
    public static ViewsController viewsController;

    public Views() {
        viewsController = new ViewsController();
        viewsList.add("index");
        viewsList.add("login");
        viewsList.add("registerAdmin");
        viewsList.add("registerMember");
        viewsList.add("adminDB");
        viewsList.add("memberDB");
        viewsList.add("addAddress");
        viewsList.add("tweet");
        viewsList.add("tweets");
        viewsList.add("deleteOwnTweets");
        viewsList.add("deleteTweets");
        viewsList.add("logout");
        viewsList.add("closeApp");
    }

    public void index() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String content = "" +
            "Welcome to the Tweeter for Console!.\n" +
            "1. Login.\n" +
            "2. Register a user as an Admin.\n" +
            "3. Register a user as a Member.\n" +
            "0. Close app";
        System.out.println(content);
        Scanner sc = new Scanner(System.in);
        System.out.print("Choose: ");
        int opt = sc.nextInt();

        String url;
        switch (opt) {
            case 1:
                url = "login";
                break;
            case 2:
                url = "registerAdmin";
                break;
            case 3:
                url = "registerMember";
                break;
            case 0:
                url = "closeApp";
                break;
            default:
                System.out.println("Invalid choice.");
                url = "index";
                break;
        }

        renderView(url, null);
    }

    public void login() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        System.out.println("\nLogin page.");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your email address: ");
        String email = sc.nextLine();
        System.out.println("Enter your password: ");
        String password = sc.nextLine();

        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        String url = viewsController.loginController(params);

        renderView(url, null);
    }

    public void registerAdmin() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        System.out.println("\nRegister as an Admin.");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your email address:");
        String email = sc.nextLine();
        System.out.println("Enter your password:");
        String password = sc.nextLine();

        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        String url = viewsController.registerAdminController(params);

        renderView(url, null); // adminDB or index
    }

    public void registerMember() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        System.out.println("\nRegister as a Member.");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your email address:");
        String email = sc.nextLine();
        System.out.println("Enter your password:");
        String password = sc.nextLine();

        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        String url = viewsController.registerMemberController(params);

        renderView(url, null); // memberDB or index
    }

    public void memberDB() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String content = "" +
                "\nWelcome to your dashboard.\n" +
                "1. Add your address.\n" +
                "2. Tweet.\n" +
                "3. Browse tweets.\n" +
                "4. Delete your tweets.\n" +
                "0. Log out.";
        System.out.println(content);
        Scanner sc = new Scanner(System.in);
        System.out.print("Choose: ");
        int opt = sc.nextInt();

        String url;
        switch (opt) {
            case 1:
                url = "addAddress";
                break;
            case 2:
                url = "tweet";
                break;
            case 3:
                url = "tweets";
                break;
            case 4:
                url = "deleteOwnTweets";
                break;
            case 0:
                url = "logout";
                break;
            default:
                System.out.println("Invalid choice.");
                url = "memberDB";
                break;
        }

        renderView(url, null);
    }

    public void adminDB() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String content = "" +
                "\nWelcome to your dashboard.\n" +
                "1. Add your address.\n" +
                "2. Tweet.\n" +
                "3. Browse tweets.\n" +
                "4. Delete tweets (admin).\n" +
                "0. Log out.";
        System.out.println(content);
        Scanner sc = new Scanner(System.in);
        System.out.print("Choose: ");
        int opt = sc.nextInt();

        String url;
        switch (opt) {
            case 1:
                url = "addAddress";
                break;
            case 2:
                url = "tweet";
                break;
            case 3:
                url = "tweets";
                break;
            case 4:
                url = "deleteTweets";
                break;
            case 0:
                url = "logout";
                break;
            default:
                System.out.println("Invalid choice.");
                url = "adminDB";
                break;
        }

        renderView(url, null);
    }

    public void addAddress() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        System.out.println("\nAddress Page.");
        String url;
        if (FileManager.searchAddress(User.theLoggedUser.getUserId())) {
            if (User.theLoggedUser.getIsAdmin()) {
                url = "adminDB";
            } else {
                url = "memberDB";
            }
        } else {
            System.out.println("Enter your home address: ");
            Scanner sc = new Scanner(System.in);
            String address = sc.nextLine();

            HashMap<String, String> params = new HashMap<>();
            params.put("address", address);

            url = viewsController.addAddressController(params);
        }

        renderView(url, null);
    }

    public void tweet() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        System.out.println("\nTweet a tweet page.");
        String url;

        System.out.println("What's on your mind today?: ");
        System.out.println("(255 characters max)");
        Scanner sc = new Scanner(System.in);
        String tweet = sc.nextLine();
        int maxLength = Math.min(tweet.length(), 255);

        HashMap<String, String> params = new HashMap<>();
        params.put("tweet", tweet.substring(0, maxLength));

        url = viewsController.tweetController(params);

        renderView(url, null);
    }

    public void tweets() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        System.out.println("\nNewsfeed page.");

        if (!FileManager.getTweets()) {
            System.out.println("There are not tweets here to see.");
        }

        String url;
        if (User.theLoggedUser.getIsAdmin()) {
            url = "adminDB";
        } else {
            url = "memberDB";
        }

        renderView(url, null);
    }

    public void deleteTweets() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        System.out.println("\nDelete any tweet from newsfeed page.");
        String url;
        if (FileManager.getTweets()) {
            System.out.print("\nEnter tweet id: ");
            Scanner sc = new Scanner(System.in);
            int tweetId = sc.nextInt();

            HashMap<String, Integer> params = new HashMap<>();
            params.put("tweetId", tweetId);

            url = viewsController.deleteTweetsController(params);

            renderView(url, null);
        } else {
            System.out.println("There are no tweets to delete.");
            if (User.theLoggedUser.getIsAdmin()) {
                url = "adminDB";
            } else {
                url = "memberDB";
            }
            renderView(url, null);
        }
    }

    public void deleteOwnTweets() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        System.out.println("\nDelete your tweets from newsfeed page.");
        String url;
        if (FileManager.searchTweets(User.theLoggedUser.getUserId())) {
            System.out.print("\nEnter tweet id: ");
            Scanner sc = new Scanner(System.in);
            int tweetId = sc.nextInt();

            HashMap<String, Integer> params = new HashMap<>();
            params.put("tweetId", tweetId);

            url = viewsController.deleteOwnTweetsController(params);

            renderView(url, null);
        } else {
            System.out.println("You don't have any tweets.");
            if (User.theLoggedUser.getIsAdmin()) {
                url = "adminDB";
            } else {
                url = "memberDB";
            }
            renderView(url, null);
        }
    }


    public void logout() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        System.out.println(User.theLoggedUser.logout());
        User.theLoggedUser = null;

        renderView("index", null);
    }

    public void closeApp() {
        String content = "goodbye...";
        System.out.println(content);

        System.exit(0);
    }
}
