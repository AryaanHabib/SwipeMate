package model;

public class listerModel {
    private final String ListerID;
    private final String ListingType;
    private final String ResID;

    public listerModel(String listerID, String listingType, String resID) {
        ListerID = listerID;
        ListingType = listingType;
        ResID = resID;
    }
    public String getListerID() {
        return ListerID;
    }
    public String getListingType() { return ListingType; }

    public String getResID() { return ResID;}

}
