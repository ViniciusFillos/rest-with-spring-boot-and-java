package io.github.vinifillos.integrationTests.controller.withJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vinifillos.configs.ConfigTest;
import io.github.vinifillos.integrationTests.dto.AccountCredentialsDto;
import io.github.vinifillos.integrationTests.dto.BookDto;
import io.github.vinifillos.integrationTests.dto.TokenDto;
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
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<BookDto> books = objectMapper.readValue(content, new TypeReference<>() {
        });

        BookDto bookOne = books.getFirst();

        assertNotNull(bookOne);
        assertNotNull(bookOne.getId());
        assertNotNull(bookOne.getAuthor());
        assertNotNull(bookOne.getTitle());
        assertNotNull(bookOne.getPrice());
        assertNotNull(bookOne.getLaunchDate());

        assertEquals(1, bookOne.getId());

        assertEquals("Working effectively with legacy code", bookOne.getTitle());
        assertEquals("Michael C. Feathers", bookOne.getAuthor());
        assertEquals(49.0D, bookOne.getPrice());

        BookDto bookNine = books.get(8);

        assertNotNull(bookNine);
        assertNotNull(bookNine.getId());
        assertNotNull(bookNine.getAuthor());
        assertNotNull(bookNine.getTitle());
        assertNotNull(bookNine.getPrice());
        assertNotNull(bookNine.getLaunchDate());

        assertEquals(9, bookNine.getId());

        assertEquals("Java Concurrency in Practice", bookNine.getTitle());
        assertEquals("Brian Goetz e Tim Peierls", bookNine.getAuthor());
        assertEquals(80.0D, bookNine.getPrice());
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

    private void mockBook() {
        bookDto.setTitle("Clean Code");
        bookDto.setAuthor("Robert C. Martin");
        bookDto.setPrice(199.9D);
        bookDto.setLaunchDate(new Date());
    }
}