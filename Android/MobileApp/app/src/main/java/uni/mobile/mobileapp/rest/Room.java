package uni.mobile.mobileapp.rest;

public class Room {
    private Integer id, houseId;
    private String name;
    private String createdAt;
    private String updatedAt;

    public Room(String name, Integer houseId) {
        this.houseId = houseId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getHouseId() {
        return houseId;
    }

    public String getName() {
        return name;
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
}
