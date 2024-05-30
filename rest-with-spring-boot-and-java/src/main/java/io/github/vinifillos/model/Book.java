package io.github.vinifillos.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "book")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "author", nullable = false, length = 100)
    private String author;
    @Column(name = "launch_date", nullable = false)
    private Date launchDate;
    @Column(name = "price", nullable = false)
    private Double price;
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    public Book() {
    }

    public Book(Long id, String author, Date lauchDate, Double price, String title) {
        this.id = id;
        this.author = author;
        this.launchDate = lauchDate;
        this.price = price;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getLauchDate() {
        return launchDate;
    }

    public void setLauchDate(Date lauchDate) {
        this.launchDate = lauchDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(author, book.author) && Objects.equals(launchDate, book.launchDate) && Objects.equals(price, book.price) && Objects.equals(title, book.title);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(author);
        result = 31 * result + Objects.hashCode(launchDate);
        result = 31 * result + Objects.hashCode(price);
        result = 31 * result + Objects.hashCode(title);
        return result;
    }
}
