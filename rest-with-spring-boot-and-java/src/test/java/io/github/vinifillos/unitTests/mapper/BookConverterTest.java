package io.github.vinifillos.unitTests.mapper;

import io.github.vinifillos.mapper.BookMapper;
import io.github.vinifillos.model.Book;
import io.github.vinifillos.model.dto.BookDto;
import io.github.vinifillos.unitTests.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookConverterTest {
    
    MockBook inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockBook();
    }

    @Test
    void parseEntityToDtoTest() {
        BookDto output = BookMapper.fromBookToDto(inputObject.mockEntity());
        assertEquals(Long.valueOf(0L), output.getKey());
        assertEquals("Title Test0", output.getTitle());
        assertEquals("Author Test0", output.getAuthor());
        assertEquals(123.45D, output.getPrice());
    }

    @Test
    void parseEntityListToDtoListTest() {
        List<BookDto> outputList = BookMapper.parseListBookToDto(inputObject.mockEntityList());
        BookDto outputZero = outputList.getFirst();

        assertEquals(Long.valueOf(0L), outputZero.getKey());
        assertEquals("Title Test0", outputZero.getTitle());
        assertEquals("Author Test0", outputZero.getAuthor());
        assertEquals(123.45D, outputZero.getPrice());

        BookDto outputSeven = outputList.get(7);

        assertEquals(Long.valueOf(7L), outputSeven.getKey());
        assertEquals("Title Test7", outputSeven.getTitle());
        assertEquals("Author Test7", outputSeven.getAuthor());
        assertEquals(123.45D, outputSeven.getPrice());

        BookDto outputTwelve = outputList.get(12);

        assertEquals(Long.valueOf(12L), outputTwelve.getKey());
        assertEquals("Title Test12", outputTwelve.getTitle());
        assertEquals("Author Test12", outputTwelve.getAuthor());
        assertEquals(123.45D, outputTwelve.getPrice());
    }

    @Test
    void parseDtoToEntityTest() {
        Book output = BookMapper.fromDtoToBook(inputObject.mockDto());
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Title Test0", output.getTitle());
        assertEquals("Author Test0", output.getAuthor());
        assertEquals(123.45D, output.getPrice());
    }

    @Test
    void parserDtoListToEntityListTest() {
        List<Book> outputList = BookMapper.parseListDtoToBook(inputObject.mockDtoList());
        Book outputZero = outputList.getFirst();

        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Title Test0", outputZero.getTitle());
        assertEquals("Author Test0", outputZero.getAuthor());
        assertEquals(123.45D, outputZero.getPrice());

        Book outputSeven = outputList.get(7);

        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Title Test7", outputSeven.getTitle());
        assertEquals("Author Test7", outputSeven.getAuthor());
        assertEquals(123.45D, outputSeven.getPrice());

        Book outputTwelve = outputList.get(12);

        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("Title Test12", outputTwelve.getTitle());
        assertEquals("Author Test12", outputTwelve.getAuthor());
        assertEquals(123.45D, outputTwelve.getPrice());
    }
}
