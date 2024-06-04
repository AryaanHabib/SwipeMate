package model;

public class seekerModel {

    private final String seekerID;
    private final String seekingType;

    public seekerModel(String seekerID, String seekingType) {
        this.seekerID = seekerID;
        this.seekingType = seekingType;
    }

    public String getSeekerID() {
        return seekerID;
    }

    public String getSeekingType() {
        return seekingType;
    }
}
