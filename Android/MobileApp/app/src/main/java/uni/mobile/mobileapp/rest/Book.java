package uni.mobile.mobileapp.rest;

public class Book {
    private Integer id;
    private String title;
    private String createdAt;
    private String updatedAt;
    private String authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private Integer isbn;
    private Integer pageCount;
    private String categories;
    private String imageLinks;
    private String country;
    private Float price;

    public String getAuthors() {
        return authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public Integer getIsbn() {
        return isbn;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public String getCategories() {
        return categories;
    }

    public String getImageLinks() {
        return imageLinks;
    }

    public String getCountry() {
        return country;
    }

    public Float getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
