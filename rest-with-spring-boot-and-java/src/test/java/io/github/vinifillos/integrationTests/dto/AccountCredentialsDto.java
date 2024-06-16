package io.github.vinifillos.integrationTests.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlRootElement
public class AccountCredentialsDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
}
