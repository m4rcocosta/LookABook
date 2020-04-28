package uni.mobile.mobileapp.rest;

public class Wall {

    private Integer id, roomId, houseId;
    private String name;
    private String createdAt;
    private String updatedAt;

    public Wall(String name, Integer roomId, Integer houseId) {
        this.name = name;
        this.roomId = roomId;
        this.houseId = houseId;
    }

    public int getId() {
        return id;
    }

    public int getRoomId() {
        return roomId;
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

    public String getUpdatedAt() {
        return updatedAt;
    }
}
