package io.github.vinifillos.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@JsonPropertyOrder({"id", "author", "launchDate", "price", "title"})
@Getter @Setter
@NoArgsConstructor
public class BookDto extends RepresentationModel<BookDto> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long key;
    private String author;
    private Date launchDate;
    private Double price;
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BookDto bookDto = (BookDto) o;
        return Objects.equals(key, bookDto.key) && Objects.equals(author, bookDto.author) && Objects.equals(launchDate, bookDto.launchDate) && Objects.equals(price, bookDto.price) && Objects.equals(title, bookDto.title);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(key);
        result = 31 * result + Objects.hashCode(author);
        result = 31 * result + Objects.hashCode(launchDate);
        result = 31 * result + Objects.hashCode(price);
        result = 31 * result + Objects.hashCode(title);
        return result;
    }
}
