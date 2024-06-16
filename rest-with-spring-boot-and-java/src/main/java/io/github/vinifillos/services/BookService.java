package io.github.vinifillos.services;

import io.github.vinifillos.controllers.BookController;
import io.github.vinifillos.exceptions.RequiredObjectIsNullException;
import io.github.vinifillos.exceptions.ResourceNotFoundException;
import io.github.vinifillos.mapper.BookMapper;
import io.github.vinifillos.model.dto.BookDto;
import io.github.vinifillos.model.dto.PersonDto;
import io.github.vinifillos.repositories.BookRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final Logger logger = Logger.getLogger(BookService.class.getName());
    final PagedResourcesAssembler<BookDto> assembler;

    public PagedModel<EntityModel<BookDto>> findAll(Pageable pageable) {
        logger.info("Finding all books!");

        var bookPage = bookRepository.findAll(pageable);
        var bookDtosPage = bookPage.map(BookMapper::fromBookToDto);
        bookDtosPage.forEach(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));
        Link link = linkTo(methodOn(BookController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(bookDtosPage, link);
    }

    public BookDto findById(Long id) {
        logger.info("Finding one book!");

        var entity = bookRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        var dto = BookMapper.fromBookToDto(entity);
        dto.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return dto;
    }


    public BookDto create(BookDto book) {
        if (book == null) throw new RequiredObjectIsNullException();
        logger.info("Creating one book!");
        var entity = BookMapper.fromDtoToBook(book);
        var dto = BookMapper.fromBookToDto(bookRepository.save(entity));
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getKey())).withSelfRel());
        return dto;
    }

    public BookDto update(BookDto book) {
        if (book == null) throw new RequiredObjectIsNullException();
        logger.info("Updating one book!");
        var entity = bookRepository.findById(book.getKey())
                .orElseThrow(ResourceNotFoundException::new);

        entity.setAuthor(book.getAuthor());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());
        entity.setLaunchDate(book.getLaunchDate());

        var dto = BookMapper.fromBookToDto(bookRepository.save(entity));
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getKey())).withSelfRel());
        return dto;
    }

    public void delete(Long id) {
        logger.info("Deleting one book!");

        var entity = bookRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        bookRepository.delete(entity);
    }
}
