package io.github.vinifillos.integrationTests.controller.withYaml;

import io.github.vinifillos.configs.ConfigTest;
import io.github.vinifillos.integrationTests.controller.withYaml.mapper.YMLMapper;
import io.github.vinifillos.integrationTests.dto.AccountCredentialsDto;
import io.github.vinifillos.integrationTests.dto.PersonDto;
import io.github.vinifillos.integrationTests.dto.TokenDto;
import io.github.vinifillos.integrationTests.dto.pagedModels.PagedModelPerson;
import io.github.vinifillos.integrationTests.dto.wrappers.WrapperPersonDto;
import io.github.vinifillos.integrationTests.testContainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YMLMapper objectMapper;
    private static PersonDto person;

    @BeforeAll
    public static void setup() {
        objectMapper = new YMLMapper();
        person = new PersonDto();
    }

    @Test
    @Order(0)
    void authorization() {
        AccountCredentialsDto user = new AccountCredentialsDto("leandro", "admin123");

        var accessToken = given()
                .config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(ConfigTest.CONTENT_TYPE_YML, ContentType.TEXT)))
                .basePath("/auth/signin")
                .port(ConfigTest.SERVER_PORT)
                .contentType(ConfigTest.CONTENT_TYPE_YML)
                .accept(ConfigTest.CONTENT_TYPE_YML)
                .body(user, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDto.class, objectMapper)
                .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(ConfigTest.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/person/v1")
                .setPort(ConfigTest.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    void testCreate() {
        mockPerson();
        var persistedPerson = given().spec(specification)
                .config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(ConfigTest.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(ConfigTest.CONTENT_TYPE_YML)
                .accept(ConfigTest.CONTENT_TYPE_YML)
                .body(person, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonDto.class, objectMapper);

        person = persistedPerson;

        assertNotNull(persistedPerson);

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());

        assertTrue(persistedPerson.getId() > 0);

        assertEquals("Vinicius", persistedPerson.getFirstName());
        assertEquals("Fillos", persistedPerson.getLastName());
        assertEquals("Irati - PR - Brazil", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
    }

    @Test
    @Order(2)
    void testUpdate() {
        person.setLastName("Fillos Souto Maior");

        var persistedPerson = given().spec(specification).
                config(RestAssuredConfig.config().
                        encoderConfig(EncoderConfig.encoderConfig().
                                encodeContentTypeAs(ConfigTest.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(ConfigTest.CONTENT_TYPE_YML)
                .accept(ConfigTest.CONTENT_TYPE_YML)
                .body(person, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonDto.class, objectMapper);

        person = persistedPerson;

        assertNotNull(persistedPerson);

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        assertNotNull(persistedPerson.getEnabled());

        assertEquals(person.getId(), persistedPerson.getId());

        assertEquals("Vinicius", persistedPerson.getFirstName());
        assertEquals("Fillos Souto Maior", persistedPerson.getLastName());
        assertEquals("Irati - PR - Brazil", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
        assertTrue(persistedPerson.getEnabled());
    }

    @Test
    @Order(3)
    void testDisableById() {
        mockPerson();

        var persistedPerson = given().spec(specification)
                .config(RestAssuredConfig.
                        config().
                        encoderConfig(EncoderConfig.encoderConfig().
                                encodeContentTypeAs(ConfigTest.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(ConfigTest.CONTENT_TYPE_YML)
                .accept(ConfigTest.CONTENT_TYPE_YML)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonDto.class, objectMapper);

        person = persistedPerson;

        assertNotNull(persistedPerson);

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        assertNotNull(persistedPerson.getEnabled());

        assertEquals(person.getId(), persistedPerson.getId());

        assertEquals("Vinicius", persistedPerson.getFirstName());
        assertEquals("Fillos Souto Maior", persistedPerson.getLastName());
        assertEquals("Irati - PR - Brazil", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
        assertFalse(persistedPerson.getEnabled());
    }

    @Test
    @Order(4)
    void testFindById() {
        mockPerson();

        var persistedPerson = given().spec(specification)
                .config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(ConfigTest.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(ConfigTest.CONTENT_TYPE_YML)
                .accept(ConfigTest.CONTENT_TYPE_YML)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonDto.class, objectMapper);

        person = persistedPerson;

        assertNotNull(persistedPerson);

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());

        assertEquals(person.getId(), persistedPerson.getId());

        assertEquals("Vinicius", persistedPerson.getFirstName());
        assertEquals("Fillos Souto Maior", persistedPerson.getLastName());
        assertEquals("Irati - PR - Brazil", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
    }

    @Test
    @Order(5)
    void testDelete() {

        given().spec(specification).
                config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(ConfigTest.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(ConfigTest.CONTENT_TYPE_YML)
                .accept(ConfigTest.CONTENT_TYPE_YML)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    void testFindAll() {
        var wrapper = given().spec(specification)
                .config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(ConfigTest.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(ConfigTest.CONTENT_TYPE_YML)
                .accept(ConfigTest.CONTENT_TYPE_YML)
                .queryParams("page", 0, "size", 3, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelPerson.class, objectMapper);

        var people = wrapper.getContent();

        PersonDto PersonOne = people.getFirst();

        assertNotNull(PersonOne.getId());
        assertNotNull(PersonOne.getFirstName());
        assertNotNull(PersonOne.getLastName());
        assertNotNull(PersonOne.getAddress());
        assertNotNull(PersonOne.getGender());
        assertNotNull(PersonOne.getEnabled());

        assertEquals(648, PersonOne.getId());

        assertEquals("Abbot", PersonOne.getFirstName());
        assertEquals("Thorndale", PersonOne.getLastName());
        assertEquals("920 Mallory Alley", PersonOne.getAddress());
        assertEquals("Male", PersonOne.getGender());
        assertFalse(PersonOne.getEnabled());

        PersonDto personTwo = people.get(2);

        assertNotNull(personTwo.getId());
        assertNotNull(personTwo.getFirstName());
        assertNotNull(personTwo.getLastName());
        assertNotNull(personTwo.getAddress());
        assertNotNull(personTwo.getGender());
        assertNotNull(personTwo.getEnabled());

        assertEquals(321, personTwo.getId());

        assertEquals("Abigale", personTwo.getFirstName());
        assertEquals("Wilber", personTwo.getLastName());
        assertEquals("4203 Dayton Trail", personTwo.getAddress());
        assertEquals("Female", personTwo.getGender());
        assertTrue(personTwo.getEnabled());
    }

    @Test
    @Order(7)
    void testFindAllWithoutToken() {
        RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
                .setBasePath("/api/person/v1")
                .setPort(ConfigTest.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given().spec(specificationWithoutToken)
                .config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(ConfigTest.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(ConfigTest.CONTENT_TYPE_YML)
                .accept(ConfigTest.CONTENT_TYPE_YML)
                .when()
                .get()
                .then()
                .statusCode(403);
    }

    @Test
    @Order(8)
    void testFindByName() {
        var wrapper = given().spec(specification)
                .config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(ConfigTest.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(ConfigTest.CONTENT_TYPE_YML)
                .accept(ConfigTest.CONTENT_TYPE_YML)
                .pathParam("firstName", "vini")
                .queryParams("page", 0, "size", 3, "direction", "asc")
                .when()
                .get("/findPeopleByName/{firstName}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelPerson.class, objectMapper);

        var people = wrapper.getContent();

        PersonDto Person = people.getFirst();

        assertNotNull(Person.getId());
        assertNotNull(Person.getFirstName());
        assertNotNull(Person.getLastName());
        assertNotNull(Person.getAddress());
        assertNotNull(Person.getGender());
        assertNotNull(Person.getEnabled());

        assertEquals(1, Person.getId());

        assertEquals("Vinicius", Person.getFirstName());
        assertEquals("Fillos", Person.getLastName());
        assertEquals("Street Alfredo Kamisnki", Person.getAddress());
        assertEquals("Male", Person.getGender());
        assertTrue(Person.getEnabled());
    }

    private void mockPerson() {
        person.setFirstName("Vinicius");
        person.setLastName("Fillos");
        person.setAddress("Irati - PR - Brazil");
        person.setGender("Male");
        person.setEnabled(true);
    }
}