package io.github.vinifillos.mapper;

import io.github.vinifillos.model.Book;
import io.github.vinifillos.model.dto.BookDto;

import java.util.ArrayList;
import java.util.List;

public class BookMapper {

    private static org.modelmapper.ModelMapper mapper = new org.modelmapper.ModelMapper();

    public static Book fromDtoToBook(BookDto dto) {
        Book book = mapper.map(dto, Book.class);
        book.setId(dto.getKey());
        return book;
    }

    public static BookDto fromBookToDto(Book book) {
        BookDto dto = mapper.map(book, BookDto.class);
        dto.setKey(book.getId());
        return dto;
    }

    public static List<Book> parseListDtoToBook(List<BookDto> dtosList) {
        List<Book> bookList = new ArrayList<>();
        for (BookDto dto : dtosList) {
            var entity = mapper.map(dto, Book.class);
            entity.setId(dto.getKey());
            bookList.add(entity);
        }
        return bookList;
    }

    public static List<BookDto> parseListBookToDto(List<Book> bookList) {
        List<BookDto> dtosList = new ArrayList<>();
        for (Book book : bookList) {
            var entity = mapper.map(book, BookDto.class);
            entity.setKey(book.getId());
            dtosList.add(entity);
        }
        return dtosList;
    }
}