package uni.mobile.mobileapp.rest;

import com.google.gson.annotations.SerializedName;


import java.util.HashMap;
import java.util.Map;
public class House {

    private Integer id;
    private String name;
    private Float lat;
    @SerializedName("long")
    private Float longt;
    private Boolean isMainHouse;
    private String createdAt;
    private String updatedAt;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public House(String name, Float lat, Float longt, Boolean isMainHouse) {
        this.name = name;
        this.lat = lat;
        this.longt = longt;
        this.isMainHouse = isMainHouse;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Float getLat() {
        return lat;
    }

    public Boolean getMainHouse() {
        return isMainHouse;
    }

    public Float getLongt() {
        return longt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
