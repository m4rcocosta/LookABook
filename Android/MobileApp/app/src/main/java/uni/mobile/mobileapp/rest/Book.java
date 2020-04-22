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

    public Book(Integer id, String title, String createdAt, String updatedAt, String authors, String publisher, String publishedDate, String description, Integer isbn, Integer pageCount, String categories, String imageLinks, String country, Float price) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = description;
        this.isbn = isbn;
        this.pageCount = pageCount;
        this.categories = categories;
        this.imageLinks = imageLinks;
        this.country = country;
        this.price = price;
    }

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
