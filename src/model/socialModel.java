package model;

public class socialModel {
    private final String emailID;
    private final String userID;
    private final String phone;
    private final String instaID;

    public socialModel(String emailID, String userID, String phone, String instaID) {
        this.emailID = emailID;
        this.userID = userID;
        this.phone = phone;
        this.instaID = instaID;
    }

    public String getEmailID() {
        return emailID;
    }

    public String getInstaID() {
        return instaID;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserID() {
        return userID;
    }
}

