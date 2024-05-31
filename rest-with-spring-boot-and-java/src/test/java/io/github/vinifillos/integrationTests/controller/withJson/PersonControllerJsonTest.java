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
    void testCreate() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(ConfigTest.HEADER_PARAM_ORIGIN, "http://vinifillos.com.br")
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

        PersonDto createdPersonDto = objectMapper.readValue(content, PersonDto.class);
        personDto = createdPersonDto;

        assertNotNull(createdPersonDto);
        assertNotNull(createdPersonDto.getId());
        assertNotNull(createdPersonDto.getFirstName());
        assertNotNull(createdPersonDto.getLastName());
        assertNotNull(createdPersonDto.getGender());
        assertNotNull(createdPersonDto.getAddress());

        assertTrue(createdPersonDto.getId() > 0);

        assertEquals("Richard", createdPersonDto.getFirstName());
        assertEquals("Stallman", createdPersonDto.getLastName());
        assertEquals("Male", createdPersonDto.getGender());
        assertEquals("New York City, New York, US", createdPersonDto.getAddress());
    }

    private void mockPerson() {
        personDto.setFirstName("Richard");
        personDto.setLastName("Stallman");
        personDto.setAddress("New York City, New York, US");
        personDto.setGender("Male");
    }
}
