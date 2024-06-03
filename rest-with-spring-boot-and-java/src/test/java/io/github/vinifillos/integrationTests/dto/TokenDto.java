package io.github.vinifillos.integrationTests.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode
@XmlRootElement(name = "tokenDto")
public class TokenDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "username")
    private String username;

    @XmlElement(name = "authenticated")
    private Boolean authenticated;

    @XmlElement(name = "created")
    private Date created;

    @XmlElement(name = "expiration")
    private Date expiration;

    @XmlElement(name = "accessToken")
    private String accessToken;

    @XmlElement(name = "refreshToken")
    private String refreshToken;
}