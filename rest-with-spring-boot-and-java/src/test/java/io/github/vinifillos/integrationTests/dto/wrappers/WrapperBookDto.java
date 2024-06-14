package io.github.vinifillos.integrationTests.dto.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@XmlRootElement
public class WrapperBookDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private BookEmbeddedDto embedded;
}
