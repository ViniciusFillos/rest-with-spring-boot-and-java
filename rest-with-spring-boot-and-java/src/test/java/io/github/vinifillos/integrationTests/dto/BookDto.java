package io.github.vinifillos.integrationTests.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class BookDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String author;
    private Date launchDate;
    private Double price;
    private String title;
}
