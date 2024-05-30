package io.github.vinifillos.services;

import io.github.vinifillos.controllers.BookController;
import io.github.vinifillos.exceptions.RequiredObjectIsNullException;
import io.github.vinifillos.exceptions.ResourceNotFoundException;
import io.github.vinifillos.mapper.BookMapper;
import io.github.vinifillos.model.dto.BookDto;
import io.github.vinifillos.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    private Logger logger = Logger.getLogger(BookService.class.getName());

    public List<BookDto> findAll() {
        logger.info("Finding all books!");

        var books = BookMapper.parseListBookToDto(bookRepository.findAll());
        books
                .stream()
                .forEach(p -> p.add(linkTo(methodOn(BookController.class ).findById(p.getKey())).withSelfRel()));
        return books;
    }

    public BookDto findById(Long id) {
        logger.info("Finding one book!");

        var entity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var dto = BookMapper.fromBookToDto(entity);
        dto.add(linkTo(methodOn(BookController.class ).findById(id)).withSelfRel());
        return dto;
    }


    public BookDto create(BookDto book) {
        if(book == null) throw new RequiredObjectIsNullException();
        logger.info("Creating one book!");
        var entity = BookMapper.fromDtoToBook(book);
        var dto = BookMapper.fromBookToDto(bookRepository.save(entity));
        dto.add(linkTo(methodOn(BookController.class ).findById(dto.getKey())).withSelfRel());
        return dto;
    }

    public BookDto update(BookDto book) {
        if(book == null) throw new RequiredObjectIsNullException();
        logger.info("Updating one book!");
        var entity = bookRepository.findById(book.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setAuthor(book.getAuthor());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());
        entity.setLauchDate(book.getLauchDate());

        var dto = BookMapper.fromBookToDto(bookRepository.save(entity));
        dto.add(linkTo(methodOn(BookController.class ).findById(dto.getKey())).withSelfRel());
        return dto;
    }

    public void delete(Long id) {
        logger.info("Deleting one book!");

        var entity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        bookRepository.delete(entity);
    }
}
