package io.github.vinifillos.controllers;

import io.github.vinifillos.model.dto.BookDto;
import io.github.vinifillos.services.BookService;
import io.github.vinifillos.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book/v1")
@Tag(name = "Books", description = "Endpoints for managing books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Finds a book", description = "Finds a book", tags = {"Books"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = BookDto.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public BookDto findById(@PathVariable(value = "id") Long id) {
        return bookService.findById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Finds all books", description = "Finds all books", tags = {"Books"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = { @Content( mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BookDto.class)))}),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<PagedModel<EntityModel<BookDto>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "3") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
        return ResponseEntity.ok(bookService.findAll(pageable));
    }

    @Operation(summary = "Creates a book", description = "Adds a new book by passing in a JSON, XML or YML representation of the book", tags = {"Books"}, responses = {
            @ApiResponse(description = "Created", responseCode = "201",
                    content = @Content(schema = @Schema(implementation = BookDto.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    @PostMapping(consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public BookDto create(@RequestBody BookDto book) {
        return bookService.create(book);
    }

    @Operation(summary = "Updates a book", description = "Updates a book by passing in a JSON, XML or YML representation of the book", tags = {"Books"}, responses = {
            @ApiResponse(description = "Updated", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = BookDto.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    @PutMapping(consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public BookDto update(@RequestBody BookDto book) {
        return bookService.update(book);
    }

    @Operation(summary = "Deletes a book", description = "Deletes a book by passing an Id", tags = {"Books"}, responses = {
            @ApiResponse(description = "No content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
