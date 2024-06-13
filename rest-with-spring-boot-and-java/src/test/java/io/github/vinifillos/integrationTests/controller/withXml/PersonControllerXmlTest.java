package io.github.vinifillos.integrationTests.controller.withXml;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.vinifillos.configs.ConfigTest;
import io.github.vinifillos.integrationTests.dto.AccountCredentialsDto;
import io.github.vinifillos.integrationTests.dto.PersonDto;
import io.github.vinifillos.integrationTests.dto.pagedModels.PagedModelPerson;
import io.github.vinifillos.integrationTests.dto.wrappers.WrapperPersonDto;
import io.github.vinifillos.integrationTests.testContainers.AbstractIntegrationTest;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import io.github.vinifillos.model.dto.security.TokenDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;


import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;
    private static PersonDto person;

    @BeforeAll
    public static void setup() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        person = new PersonDto();
    }

    @Test
    @Order(0)
     void authorization() {

        AccountCredentialsDto user = new AccountCredentialsDto("leandro", "admin123");

        var accessToken = given()
                .basePath("/auth/signin")
                .port(ConfigTest.SERVER_PORT)
                .contentType(ConfigTest.CONTENT_TYPE_XML)
                .accept(ConfigTest.CONTENT_TYPE_XML)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDto.class)
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
     void testCreate() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(ConfigTest.CONTENT_TYPE_XML)
                .accept(ConfigTest.CONTENT_TYPE_XML)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDto persistedPerson = objectMapper.readValue(content, PersonDto.class);
        person = persistedPerson;

        assertNotNull(persistedPerson);

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        assertNotNull(persistedPerson.getEnabled());

        assertTrue(persistedPerson.getId() > 0);

        assertEquals("Vinicius", persistedPerson.getFirstName());
        assertEquals("Fillos", persistedPerson.getLastName());
        assertEquals("Irati - PR - Brazil", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
        assertTrue(persistedPerson.getEnabled());
    }

    @Test
    @Order(2)
     void testUpdate() throws JsonProcessingException {
        person.setLastName("Fillos Souto Maior");

        var content = given().spec(specification)
                .contentType(ConfigTest.CONTENT_TYPE_XML)
                .accept(ConfigTest.CONTENT_TYPE_XML)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDto persistedPerson = objectMapper.readValue(content, PersonDto.class);
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
    void testDisableById() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(ConfigTest.CONTENT_TYPE_XML)
                .accept(ConfigTest.CONTENT_TYPE_XML)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDto persistedPerson = objectMapper.readValue(content, PersonDto.class);
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
     void testFindById() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(ConfigTest.CONTENT_TYPE_XML)
                .accept(ConfigTest.CONTENT_TYPE_XML)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDto persistedPerson = objectMapper.readValue(content, PersonDto.class);
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
    @Order(5)
    void testDelete() {

        given().spec(specification)
                .contentType(ConfigTest.CONTENT_TYPE_XML)
                .accept(ConfigTest.CONTENT_TYPE_XML)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    void testFindAll() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(ConfigTest.CONTENT_TYPE_XML)
                .accept(ConfigTest.CONTENT_TYPE_XML)
                .queryParams("page", 0, "size", 3, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
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
                .contentType(ConfigTest.CONTENT_TYPE_XML)
                .accept(ConfigTest.CONTENT_TYPE_XML)
                .when()
                .get()
                .then()
                .statusCode(403);
    }

    private void mockPerson() {
        person.setFirstName("Vinicius");
        person.setLastName("Fillos");
        person.setAddress("Irati - PR - Brazil");
        person.setGender("Male");
        person.setEnabled(true);
    }

    @Test
    @Order(8)
    void testFindByName() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(ConfigTest.CONTENT_TYPE_XML)
                .accept(ConfigTest.CONTENT_TYPE_XML)
                .pathParam("firstName", "vini")
                .queryParams("page", 0, "size", 3, "direction", "asc")
                .when()
                .get("/findPeopleByName/{firstName}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
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
}