package uni.mobile.mobileapp.rest;

import com.google.gson.annotations.SerializedName;

public class House {

    private int id;

    private String name;


    private float lat;

    @SerializedName("long")
    private float longt;

    private Boolean isMainHouse;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getLat() {
        return lat;
    }

    public Boolean getMainHouse() {
        return isMainHouse;
    }

    public float getLongt() {
        return longt;
    }
}
