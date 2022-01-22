package vendor.app;

import vendor.app.traits.Accessible;

import java.util.HashMap;

public class User implements Accessible {
    protected String email;
    protected String password;
    protected boolean isAdmin;
    public static final HashMap<Integer, User> registeredUsers = new HashMap<>();
    public static User theLoggedUser = null;
    protected int userId;
    public static int userCounter = 0;

    public User() {
        this.isAdmin = false;
        userId = ++userCounter;
        registeredUsers.put(userId, this);
    }

    public int getUserId() {
        return userId;
    }

    public String login() {
        User.theLoggedUser = this;
        return "logging in as a user...";
    }

    public String logout() {
        return this.email + " has logged out...";
    }

    public User setPassword(String string)
    {
        this.password = string;
        return this;
    }

    public String getPassword()
    {
        return this.password;
    }

    public User setEmail(String string)
    {
        this.email = string;
        return this;
    }

    public String getEmail()
    {
        return this.email;
    }

    public boolean getIsAdmin()
    {
        return this.isAdmin;
    }
}
