package vendor.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import vendor.app.Admin;
import vendor.app.Member;
import vendor.app.User;
import vendor.app.Validator;
import vendor.app.helpers.Decoder;
import vendor.app.helpers.Encoder;
import vendor.app.helpers.FileManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ViewsController implements Encoder, Decoder {
    public static HashMap<String, String> params;

    public String loginController(HashMap<String, String> params) {
        ViewsController.params = params;

        AtomicBoolean validUser = new AtomicBoolean(false);
        AtomicInteger userId = new AtomicInteger();
        User.registeredUsers.forEach((id, user) -> {
            if (user.getPassword().equals(decodeToBase64(hash256(params.get("password")))) && user.getEmail().equals(params.get("email"))) {
                validUser.set(true);
                userId.set(id);
            }
        });

        String url;
        if (validUser.get()) {
            User.theLoggedUser = User.registeredUsers.get(userId.get());
            if (User.registeredUsers.get(userId.get()).getIsAdmin()) {
                url = "adminDB";
            } else {
                url = "memberDB";
            }
        } else {
            System.out.println("Invalid user credentials.");
            System.out.println("1. re-enter information");
            System.out.println("0. back to main menu");
            Scanner sc = new Scanner(System.in);
            int opt = sc.nextInt();

            switch (opt) {
                case 1:
                    url = "login";
                    break;
                case 0:
                default:
                    url = "index";
                    break;
            }
        }

        return url;
    }

    public String registerAdminController(HashMap<String, String> params) {
        AtomicReference<String> url = new AtomicReference<>("index");
        User.registeredUsers.forEach((id, user) -> {
            if (user.getEmail().equals(params.get("email"))) {
                url.set("registerAdmin");
            }
        });
        if (!url.get().equals("index")) {
            System.out.println("The email is already in use.");
            System.out.println("Please change your input.");
            return url.get();
        }

        // Creating rules for the fields
        HashMap<String, String> rules = new HashMap<>();
        rules.put("email", "required|email");
        rules.put("password", "required|min:8");

        // passing the input
        HashMap<String, String> data = new HashMap<>();
        data.put("email", params.get("email"));
        data.put("password", params.get("password"));

        Validator validator = new Validator();

        if (validator.validate(data, rules).get()) {
            Admin user = new Admin();

            String encodedPW = decodeToBase64(hash256(data.get("password")));

            user.setEmail(data.get("email"))
                    .setPassword(encodedPW);

            System.out.println("The user " + user.getEmail() + " has signed up successfully!");
            System.out.println("And his hashed password is " + user.getPassword());
            System.out.println("Now the password will be securely stored in the database.");

            url.set("login");
        } else {
            System.out.println(validator.getErrors().values());
            System.out.println("1. re-enter information");
            System.out.println("0. back to main menu");
            Scanner sc = new Scanner(System.in);
            int opt = sc.nextInt();

            switch (opt) {
                case 1:
                    url.set("registerAdmin");
                    break;
                case 0:
                default:
                    url.set("index");
                    break;
            }
        }

        return url.get();
    }

    public String registerMemberController(HashMap<String, String> params) {
        // Creating rules for the fields
        HashMap<String, String> rules = new HashMap<>();
        rules.put("email", "required|email");
        rules.put("password", "required|min:8");

        // passing the input
        HashMap<String, String> data = new HashMap<>();
        data.put("email", params.get("email"));
        data.put("password", params.get("password"));

        Validator validator = new Validator();
        String url;

        if (validator.validate(data, rules).get()) {
            Member user = new Member();

            String encodedPW = decodeToBase64(hash256(data.get("password")));

            user.setEmail(data.get("email"))
                    .setPassword(encodedPW);

            System.out.println("The user " + user.getEmail() + " has signed up successfully!");
            System.out.println("And his hashed password is " + user.getPassword());
            System.out.println("Now the password will be securely stored in the database.");

            url = "login";
        } else {
            System.out.println(validator.getErrors().values());
            System.out.println("1. re-enter information");
            System.out.println("0. back to main menu");
            Scanner sc = new Scanner(System.in);
            int opt = sc.nextInt();

            switch (opt) {
                case 1:
                    url = "registerMember";
                    break;
                case 0:
                default:
                    url = "index";
                    break;
            }
        }

        return url;
    }

    public String addAddressController(HashMap<String, String> params) {
        int userId = User.theLoggedUser.getUserId();
        String address = params.get("address");

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("userId", userId);
        node.put("address", address);

        FileManager.create("addressList", node);

        if (User.theLoggedUser.getIsAdmin()) {
            return "adminDB";
        } else {
            return "memberDB";
        }
    }

    public String tweetController(HashMap<String, String> params) {
        int userId = User.theLoggedUser.getUserId();
        String tweet = params.get("tweet");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("userId", userId);
        node.put("tweetContent", tweet);
        node.put("date", date);

        FileManager.create("tweetsList", node);

        if (User.theLoggedUser.getIsAdmin()) {
            return "adminDB";
        } else {
            return "memberDB";
        }
    }

    public String deleteOwnTweetsController(HashMap<String, Integer> params) {
        int tweetId = params.get("tweetId");

        FileManager.delete(tweetId, User.theLoggedUser.getUserId());

        if (User.theLoggedUser.getIsAdmin()) {
            return "adminDB";
        } else {
            return "memberDB";
        }
    }

    public String deleteTweetsController(HashMap<String, Integer> params) {
        int tweetId = params.get("tweetId");

        FileManager.delete(tweetId);

        if (User.theLoggedUser.getIsAdmin()) {
            return "adminDB";
        } else {
            return "memberDB";
        }
    }
}
