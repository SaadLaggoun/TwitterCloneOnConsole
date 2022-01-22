package vendor.app;

public class Member extends User {

    public Member() {
        this.isAdmin = false;
    }

    @Override
    public String login() {
        String msg = super.login();
        return msg + "  and Setting a flag for online members";
    }
}
