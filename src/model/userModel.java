package model;

public class userModel {
    private final String UserID;
    private final String Name;
    private final String Gender;
    private final String Bio;
    private final int age;

    public userModel(String UserID, String Name, String Gender, String Bio, int age) {
        this.UserID = UserID;
        this.Name = Name;
        this.Gender = Gender;
        this.Bio = Bio;
        this.age = age;
    }

    public String getName() {
        return Name;
    }

    public String getGender() {
        return Gender;
    }

    public int getAge() {
        return age;
    }

    public String getBio() {
        return Bio;
    }

    public String getUserID() {
        return UserID;
    }
}
