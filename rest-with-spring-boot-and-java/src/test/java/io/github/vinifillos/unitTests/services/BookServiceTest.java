package io.github.vinifillos.unitTests.services;

import io.github.vinifillos.exceptions.RequiredObjectIsNullException;
import io.github.vinifillos.model.Book;
import io.github.vinifillos.model.dto.BookDto;
import io.github.vinifillos.repositories.BookRepository;
import io.github.vinifillos.services.BookService;
import io.github.vinifillos.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    MockBook input;

    @InjectMocks
    private BookService service;

    @Mock
    BookRepository bookRepository;

    @BeforeEach
    void setUpMocks() {
        input = new MockBook();
    }

    @Test
    void findById_WithValidData_ReturnsBook() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(entity));

        var result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Title Test1", result.getTitle());
        assertEquals("Author Test1", result.getAuthor());
        assertEquals(123.45D, result.getPrice());
        assertNotNull(result.getLaunchDate());
    }

    @Test
    void create_WithValidData_ReturnBook() {
        Book persisted = input.mockEntity(1);
        persisted.setId(1L);
        BookDto dto = input.mockDto(1);
        dto.setKey(1L);
        when(bookRepository.save(any(Book.class))).thenReturn(persisted);

        var result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Title Test1", result.getTitle());
        assertEquals("Author Test1", result.getAuthor());
        assertEquals(123.45D, result.getPrice());
        assertNotNull(result.getLaunchDate());
    }

    @Test
    void create_WithNullBook_ReturnException() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> service.create(null));
        String expectedMessage = "It's not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update_WithValidData_ReturnBook() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);
        BookDto dto = input.mockDto(1);
        dto.setKey(1L);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(bookRepository.save(entity)).thenReturn(entity);

        var result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Title Test1", result.getTitle());
        assertEquals("Author Test1", result.getAuthor());
        assertEquals(123.45D, result.getPrice());
        assertNotNull(result.getLaunchDate());
    }

    @Test
    void update_WithNullBook_ReturnException() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> service.update(null));
        String expectedMessage = "It's not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete_WithValidData_DoNotReturnException() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(entity));

        assertDoesNotThrow(() -> service.delete(1L));
    }
}