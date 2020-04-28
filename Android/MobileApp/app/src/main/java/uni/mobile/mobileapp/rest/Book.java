package uni.mobile.mobileapp.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import uni.mobile.mobileapp.rest.googleJson.GoogleData;

public class Book {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("authors")
    @Expose
    private String authors;
    @SerializedName("publisher")
    @Expose
    private Object publisher;
    @SerializedName("publishedDate")
    @Expose
    private Object publishedDate;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("isbn")
    @Expose
    private Object isbn;
    @SerializedName("pageCount")
    @Expose
    private Object pageCount;
    @SerializedName("categories")
    @Expose
    private Object categories;
    @SerializedName("imageLinks")
    @Expose
    private Object imageLinks;
    @SerializedName("country")
    @Expose
    private Object country;
    @SerializedName("price")
    @Expose
    private Object price;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("shelf_id")
    @Expose
    private Integer shelfId;
    @SerializedName("googleData")
    @Expose
    private GoogleData googleData;

    private Integer wallId, roomId, houseId;

    public Book(String title, String authors, Object isbn, Integer shelfId, Integer wallId, Integer roomId, Integer houseId) {
        this.title = title;
        this.authors = authors;
        this.isbn = isbn;
        this.shelfId = shelfId;
        this.wallId = wallId;
        this.roomId = roomId;
        this.houseId = houseId;
    }

    public Integer getId() {
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

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public Object getPublisher() {
        return publisher;
    }

    public void setPublisher(Object publisher) {
        this.publisher = publisher;
    }

    public Object getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Object publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getIsbn() {
        return isbn;
    }

    public void setIsbn(Object isbn) {
        this.isbn = isbn;
    }

    public Object getPageCount() {
        return pageCount;
    }

    public void setPageCount(Object pageCount) {
        this.pageCount = pageCount;
    }

    public Object getCategories() {
        return categories;
    }

    public void setCategories(Object categories) {
        this.categories = categories;
    }

    public Object getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(Object imageLinks) {
        this.imageLinks = imageLinks;
    }

    public Object getCountry() {
        return country;
    }

    public void setCountry(Object country) {
        this.country = country;
    }

    public Object getPrice() {
        return price;
    }

    public void setPrice(Object price) {
        this.price = price;
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

    public Integer getShelfId() {
        return shelfId;
    }

    public void setShelfId(Integer shelfId) {
        this.shelfId = shelfId;
    }

    public GoogleData getGoogleData() {
        return googleData;
    }

    public void setGoogleData(GoogleData googleData) {
        this.googleData = googleData;
    }

}