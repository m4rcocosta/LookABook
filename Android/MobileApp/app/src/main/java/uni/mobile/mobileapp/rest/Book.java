package uni.mobile.mobileapp.rest;

import uni.mobile.mobileapp.rest.googleJson.GoogleData;

public class Book {
    private Integer id;
    private String title;
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
    private Integer shelfId;
    private GoogleData googleData;

    public Book( String title,  String authors, String publisher, String publishedDate, String description, Integer isbn, Integer pageCount, String categories, String imageLinks, String country, Float price, Integer shelfId) {
        this.title = title;
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
        this.shelfId = shelfId;

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

    public GoogleData getGoogleData() {
        return googleData;
    }
}
