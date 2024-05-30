package io.github.vinifillos.repositories;

import io.github.vinifillos.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
