package io.github.vinifillos.integrationTests.dto.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.vinifillos.integrationTests.dto.PersonDto;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class PersonEmbeddedDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("personDtoList")
    private List<PersonDto> people;
}