package io.github.vinifillos.integrationTests.controller.withJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vinifillos.configs.ConfigTest;
import io.github.vinifillos.integrationTests.dto.AccountCredentialsDto;
import io.github.vinifillos.integrationTests.dto.BookDto;
import io.github.vinifillos.integrationTests.dto.TokenDto;
import io.github.vinifillos.integrationTests.dto.wrappers.WrapperBookDto;
import io.github.vinifillos.integrationTests.testContainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static BookDto bookDto;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        bookDto = new BookDto();
    }

    @Test
    @Order(0)
    void authorization() {

        AccountCredentialsDto user = new AccountCredentialsDto("leandro", "admin123");

        var accessToken = given()
                .basePath("/auth/signin")
                .port(ConfigTest.SERVER_PORT)
                .contentType(ConfigTest.CONTENT_TYPE_JSON)
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
                .setBasePath("/api/book/v1")
                .setPort(ConfigTest.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    void testCreate() throws JsonProcessingException {
        mockBook();

        var content = given().spec(specification)
                .contentType(ConfigTest.CONTENT_TYPE_JSON)
                .body(bookDto)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookDto persistedBookDto = objectMapper.readValue(content, BookDto.class);
        bookDto = persistedBookDto;

        assertNotNull(persistedBookDto);
        assertNotNull(persistedBookDto.getId());
        assertNotNull(persistedBookDto.getAuthor());
        assertNotNull(persistedBookDto.getTitle());
        assertNotNull(persistedBookDto.getPrice());
        assertNotNull(persistedBookDto.getLaunchDate());

        assertTrue(persistedBookDto.getId() > 0);

        assertEquals("Robert C. Martin", persistedBookDto.getAuthor());
        assertEquals("Clean Code", persistedBookDto.getTitle());
        assertEquals(199.9D, persistedBookDto.getPrice());
    }

    @Test
    @Order(2)
    void testUpdate() throws JsonProcessingException {
        bookDto.setTitle("Modified Title");

        var content = given().spec(specification)
                .contentType(ConfigTest.CONTENT_TYPE_JSON)
                .body(bookDto)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookDto persistedBookDto = objectMapper.readValue(content, BookDto.class);
        bookDto = persistedBookDto;

        assertNotNull(persistedBookDto);
        assertNotNull(persistedBookDto.getId());
        assertNotNull(persistedBookDto.getAuthor());
        assertNotNull(persistedBookDto.getTitle());
        assertNotNull(persistedBookDto.getPrice());
        assertNotNull(persistedBookDto.getLaunchDate());

        assertEquals(bookDto.getId(), persistedBookDto.getId());


        assertEquals(199.9D, persistedBookDto.getPrice());
        assertEquals("Modified Title", persistedBookDto.getTitle());
        assertEquals("Robert C. Martin", persistedBookDto.getAuthor());
    }

    @Test
    @Order(3)
    void testFindById() throws JsonProcessingException {
        mockBook();

        var content = given().spec(specification)
                .contentType(ConfigTest.CONTENT_TYPE_JSON)
                .pathParam("id", bookDto.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();


        BookDto persistedBookDto = objectMapper.readValue(content, BookDto.class);
        bookDto = persistedBookDto;

        assertNotNull(persistedBookDto);
        assertNotNull(persistedBookDto.getId());
        assertNotNull(persistedBookDto.getAuthor());
        assertNotNull(persistedBookDto.getTitle());
        assertNotNull(persistedBookDto.getPrice());
        assertNotNull(persistedBookDto.getLaunchDate());

        assertEquals(bookDto.getId(), persistedBookDto.getId());

        assertEquals(199.9D, persistedBookDto.getPrice());
        assertEquals("Modified Title", persistedBookDto.getTitle());
        assertEquals("Robert C. Martin", persistedBookDto.getAuthor());
    }

    @Test
    @Order(4)
    void testDelete() {
        given().spec(specification)
                .contentType(ConfigTest.CONTENT_TYPE_JSON)
                .pathParam("id", bookDto.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    void testFindAll() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(ConfigTest.CONTENT_TYPE_JSON)
                .queryParams("page", 0, "size", 7, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperBookDto wrapper = objectMapper.readValue(content, WrapperBookDto.class);
        var books = wrapper.getEmbedded().getBooks();

        BookDto bookOne = books.getFirst();

        assertNotNull(bookOne);
        assertNotNull(bookOne.getId());
        assertNotNull(bookOne.getAuthor());
        assertNotNull(bookOne.getTitle());
        assertNotNull(bookOne.getPrice());
        assertNotNull(bookOne.getLaunchDate());

        assertEquals(12, bookOne.getId());

        assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana", bookOne.getTitle());
        assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", bookOne.getAuthor());
        assertEquals(54.0D, bookOne.getPrice());

        BookDto bookNine = books.get(4);

        assertNotNull(bookNine);
        assertNotNull(bookNine.getId());
        assertNotNull(bookNine.getAuthor());
        assertNotNull(bookNine.getTitle());
        assertNotNull(bookNine.getPrice());
        assertNotNull(bookNine.getLaunchDate());

        assertEquals(8, bookNine.getId());

        assertEquals("Domain Driven Design", bookNine.getTitle());
        assertEquals("Eric Evans", bookNine.getAuthor());
        assertEquals(92.0D, bookNine.getPrice());
    }

    @Test
    @Order(6)
    void testFindAllWithoutToken() {
        RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
                .setBasePath("/api/book/v1")
                .setPort(ConfigTest.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given().spec(specificationWithoutToken)
                .contentType(ConfigTest.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(403);
    }

    @Test
    @Order(9)
    void testHATEOAS() {
        var content = given().spec(specification)
                .contentType(ConfigTest.CONTENT_TYPE_JSON)
                .queryParams("page", 0, "size", 3, "direction", "desc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/book/v1/1\"}}}"));
        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/book/v1/6\"}}}"));
        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/book/v1/14\"}}}"));

        assertTrue(content.contains("\"page\":{\"size\":3,\"totalElements\":15,\"totalPages\":5,\"number\":0}}"));

        assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8888/api/book/v1?direction=asc&page=4&size=3&sort=title,desc\"}}"));
        assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8888/api/book/v1?direction=asc&page=1&size=3&sort=title,desc\"}"));
        assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/book/v1?page=0&size=3&direction=asc\"}"));
        assertTrue(content.contains("{\"first\":{\"href\":\"http://localhost:8888/api/book/v1?direction=asc&page=0&size=3&sort=title,desc\"}"));
    }

    private void mockBook() {
        bookDto.setTitle("Clean Code");
        bookDto.setAuthor("Robert C. Martin");
        bookDto.setPrice(199.9D);
        bookDto.setLaunchDate(new Date());
    }
}