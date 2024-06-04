package model;

public class preferenceModel {
    private final String preferencesID;
    private final String gender;
    private final String ageRange;
    private final String lifeStyle;
    private final String location;

    public preferenceModel(String preferencesID, String gender, String ageRange, String lifeStyle, String location) {
        this.preferencesID = preferencesID;
        this.gender = gender;
        this.ageRange = ageRange;
        this.lifeStyle = lifeStyle;
        this.location = location;
    }

    public String getPreferencesID() {
        return preferencesID;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public String getGender() {
        return gender;
    }

    public String getLifeStyle() {
        return lifeStyle;
    }

    public String getLocation() {
        return location;
    }
}
