package io.github.vinifillos.integrationTests.dto.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.vinifillos.integrationTests.dto.BookDto;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class BookEmbeddedDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("bookDtoList")
    private List<BookDto> books;
}