package io.github.vinifillos.integrationTests.controller.withJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vinifillos.configs.ConfigTest;
import io.github.vinifillos.integrationTests.dto.PersonDto;
import io.github.vinifillos.integrationTests.testContainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonDto personDto;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        personDto = new PersonDto();
    }

    @Test
    @Order(1)
    void CreatePerson_WithValidCors_ReturnsPerson() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(ConfigTest.HEADER_PARAM_ORIGIN, ConfigTest.ORIGIN_VINIFILLOS)
                .setBasePath("/api/person/v1")
                .setPort(ConfigTest.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given()
                .spec(specification)
                .contentType(ConfigTest.CONTENT_TYPE_JSON)
                .body(personDto)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDto persistedPersonDto = objectMapper.readValue(content, PersonDto.class);
        personDto = persistedPersonDto;

        assertNotNull(persistedPersonDto);
        assertNotNull(persistedPersonDto.getId());
        assertNotNull(persistedPersonDto.getFirstName());
        assertNotNull(persistedPersonDto.getLastName());
        assertNotNull(persistedPersonDto.getGender());
        assertNotNull(persistedPersonDto.getAddress());

        assertTrue(persistedPersonDto.getId() > 0);

        assertEquals("Richard", persistedPersonDto.getFirstName());
        assertEquals("Stallman", persistedPersonDto.getLastName());
        assertEquals("Male", persistedPersonDto.getGender());
        assertEquals("New York City, New York, US", persistedPersonDto.getAddress());
    }

    @Test
    @Order(2)
    void CreatePerson_WithInvalidCors_ReturnsInvalidCORSRequest() {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(ConfigTest.HEADER_PARAM_ORIGIN, ConfigTest.ORIGIN_INVALID)
                .setBasePath("/api/person/v1")
                .setPort(ConfigTest.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given()
                .spec(specification)
                .contentType(ConfigTest.CONTENT_TYPE_JSON)
                .body(personDto)
                .when()
                .post()
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(3)
    void FindPerson_WithValidCORS_ReturnsPerson() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(ConfigTest.HEADER_PARAM_ORIGIN, ConfigTest.ORIGIN_VINIFILLOS)
                .setBasePath("/api/person/v1")
                .setPort(ConfigTest.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given()
                .spec(specification)
                .contentType(ConfigTest.CONTENT_TYPE_JSON)
                .pathParam("id", personDto.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDto persistedPersonDto = objectMapper.readValue(content, PersonDto.class);
        personDto = persistedPersonDto;

        assertNotNull(persistedPersonDto);
        assertNotNull(persistedPersonDto.getId());
        assertNotNull(persistedPersonDto.getFirstName());
        assertNotNull(persistedPersonDto.getLastName());
        assertNotNull(persistedPersonDto.getGender());
        assertNotNull(persistedPersonDto.getAddress());

        assertTrue(persistedPersonDto.getId() > 0);

        assertEquals("Richard", persistedPersonDto.getFirstName());
        assertEquals("Stallman", persistedPersonDto.getLastName());
        assertEquals("Male", persistedPersonDto.getGender());
        assertEquals("New York City, New York, US", persistedPersonDto.getAddress());
    }

    @Test
    @Order(3)
    void FindPerson_WithInvalidCORS_ReturnsInvalidCORSRequest() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(ConfigTest.HEADER_PARAM_ORIGIN, ConfigTest.ORIGIN_INVALID)
                .setBasePath("/api/person/v1")
                .setPort(ConfigTest.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given()
                .spec(specification)
                .contentType(ConfigTest.CONTENT_TYPE_JSON)
                .pathParam("id", personDto.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    private void mockPerson() {
        personDto.setFirstName("Richard");
        personDto.setLastName("Stallman");
        personDto.setAddress("New York City, New York, US");
        personDto.setGender("Male");
    }
}
