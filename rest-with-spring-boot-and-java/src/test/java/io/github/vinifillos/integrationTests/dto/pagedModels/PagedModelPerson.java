package io.github.vinifillos.integrationTests.dto.pagedModels;

import io.github.vinifillos.integrationTests.dto.PersonDto;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@XmlRootElement
public class PagedModelPerson {

    @XmlElement(name = "content")
    private List<PersonDto> content;
}
