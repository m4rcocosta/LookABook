package uni.mobile.mobileapp.rest;

public class Wall {

    private Integer id;
    private String name;
    private String createdAt;
    private String updatedAt;

    public Wall( String name) {
        this.name = name;
    }

    public int getId() {
        return id;
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
