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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        List<Book> list = input.mockEntityList();
        when(bookRepository.findAll()).thenReturn(list);

        var books = service.findAll();

        assertNotNull(books);
        assertEquals(14,books.size());

        var bookZero = books.get(0);
        var bookSeven = books.get(7);
        var bookFourteen = books.get(13);

        assertNotNull(bookZero);
        assertNotNull(bookZero.getKey());
        assertNotNull(bookZero.getLinks());
        assertTrue(bookZero.toString().contains("links: [</api/book/v1/0>;rel=\"self\"]"));
        assertEquals("Title Test0", bookZero.getTitle());
        assertEquals("Author Test0", bookZero.getAuthor());
        assertEquals(123.45D, bookZero.getPrice());
        assertNotNull(bookZero.getLauchDate());

        assertNotNull(bookSeven);
        assertNotNull(bookSeven.getKey());
        assertNotNull(bookSeven.getLinks());
        assertTrue(bookSeven.toString().contains("links: [</api/book/v1/7>;rel=\"self\"]"));
        assertEquals("Title Test7", bookSeven.getTitle());
        assertEquals("Author Test7", bookSeven.getAuthor());
        assertEquals(123.45D, bookSeven.getPrice());
        assertNotNull(bookSeven.getLauchDate());


        assertNotNull(bookFourteen);
        assertNotNull(bookFourteen.getKey());
        assertNotNull(bookFourteen.getLinks());
        assertTrue(bookFourteen.toString().contains("links: [</api/book/v1/13>;rel=\"self\"]"));
        assertEquals("Title Test13", bookFourteen.getTitle());
        assertEquals("Author Test13", bookFourteen.getAuthor());
        assertEquals(123.45D, bookFourteen.getPrice());
        assertNotNull(bookFourteen.getLauchDate());
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
        assertNotNull(result.getLauchDate());

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
        assertNotNull(result.getLauchDate());
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
        assertNotNull(result.getLauchDate());
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