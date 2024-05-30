package io.github.vinifillos.unitTests.mocks;

import io.github.vinifillos.model.Book;
import io.github.vinifillos.model.dto.BookDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MockBook {


    public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookDto mockDto() {
        return mockDto(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookDto> mockDtoList() {
        List<BookDto> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockDto(i));
        }
        return books;
    }
    
    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setTitle("Title Test" + number);
        book.setAuthor("Author Test" + number);
        book.setPrice(123.45D);
        book.setLauchDate(new Date());
        book.setId(number.longValue());
        return book;
    }

    public BookDto mockDto(Integer number) {
        BookDto book = new BookDto();
        book.setTitle("Title Test" + number);
        book.setAuthor("Author Test" + number);
        book.setPrice(123.45D);
        book.setLauchDate(new Date());
        book.setKey(number.longValue());
        return book;
    }

}
