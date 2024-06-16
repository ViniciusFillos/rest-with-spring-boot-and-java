package io.github.vinifillos.integrationTests.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@XmlRootElement
@Data
public class PersonDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private Boolean enabled;
}
