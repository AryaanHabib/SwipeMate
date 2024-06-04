package model;

public class requirementModel {
    private final String requirementID;
    private final String gender;
    private final String ageRange;
    private final String lifeStyle;
    private final String location;

    public requirementModel(String requirementID, String gender, String ageRange, String lifeStyle, String location) {
        this.requirementID = requirementID;
        this.gender = gender;
        this.ageRange = ageRange;
        this.lifeStyle = lifeStyle;
        this.location = location;
    }

    public String getRequirementID() {
        return requirementID;
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


