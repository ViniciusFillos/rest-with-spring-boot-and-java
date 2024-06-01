package io.github.vinifillos.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Setter @Getter
@NoArgsConstructor
@JsonPropertyOrder({"id", "firstName", "lastName", "address", "gender"})
public class PersonDto extends RepresentationModel<PersonDto> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long key;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PersonDto personDto = (PersonDto) o;
        return Objects.equals(key, personDto.key) && Objects.equals(firstName, personDto.firstName) && Objects.equals(lastName, personDto.lastName) && Objects.equals(address, personDto.address) && Objects.equals(gender, personDto.gender);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(key);
        result = 31 * result + Objects.hashCode(firstName);
        result = 31 * result + Objects.hashCode(lastName);
        result = 31 * result + Objects.hashCode(address);
        result = 31 * result + Objects.hashCode(gender);
        return result;
    }
}
