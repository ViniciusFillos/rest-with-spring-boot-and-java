package io.github.vinifillos.integrationTests.controller.withYaml;

import io.github.vinifillos.configs.ConfigTest;
import io.github.vinifillos.integrationTests.controller.withYaml.mapper.YMLMapper;
import io.github.vinifillos.integrationTests.dto.AccountCredentialsDto;
import io.github.vinifillos.integrationTests.dto.TokenDto;
import io.github.vinifillos.integrationTests.testContainers.AbstractIntegrationTest;
import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerYamlTest extends AbstractIntegrationTest {

    private static YMLMapper objectMapper;
    private static TokenDto tokenDto;

    @BeforeAll
    public static void setup() {
        objectMapper = new YMLMapper();
    }

    @Test
    @Order(1)
    void testSignIn() {

        AccountCredentialsDto user =
                new AccountCredentialsDto("leandro", "admin123");

        RequestSpecification specification = new RequestSpecBuilder()
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        tokenDto = given().spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                ConfigTest.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .accept(ConfigTest.CONTENT_TYPE_YML)
                .basePath("/auth/signin")
                .port(ConfigTest.SERVER_PORT)
                .contentType(ConfigTest.CONTENT_TYPE_YML)
                .body(user, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDto.class, objectMapper);

        Assertions.assertNotNull(tokenDto.getAccessToken());
        Assertions.assertNotNull(tokenDto.getRefreshToken());
    }

    @Test
    @Order(2)
    void testRefresh() {

        var newTokenVO = given()
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                ConfigTest.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .accept(ConfigTest.CONTENT_TYPE_YML)
                .basePath("/auth/refresh")
                .port(ConfigTest.SERVER_PORT)
                .contentType(ConfigTest.CONTENT_TYPE_YML)
                .pathParam("username", tokenDto.getUsername())
                .header(ConfigTest.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getRefreshToken())
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDto.class, objectMapper);

        Assertions.assertNotNull(newTokenVO.getAccessToken());
        Assertions.assertNotNull(newTokenVO.getRefreshToken());
    }
}