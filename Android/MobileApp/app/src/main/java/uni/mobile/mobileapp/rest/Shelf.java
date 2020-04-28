package uni.mobile.mobileapp.rest;

public class Shelf {
    private Integer id, wallId, roomId, houseId;
    private String name;
    private String createdAt;
    private String updatedAt;

    public Shelf(String name, Integer wallId, Integer roomId, Integer houseId) {
        this.id = id;
        this.wallId = wallId;
        this.roomId = roomId;
        this.houseId = houseId;
    }

    public int getId() {
        return id;
    }

    public int getWallId() {
        return wallId;
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
