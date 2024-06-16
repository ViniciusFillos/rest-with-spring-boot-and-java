package io.github.vinifillos.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "book")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Book implements Serializable {

    @Serial
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
