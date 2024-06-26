package io.github.vinifillos.integrationTests.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement
@Data
public class BookDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String author;
    private Date launchDate;
    private Double price;
    private String title;
}
