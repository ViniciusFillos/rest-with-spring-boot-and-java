package io.github.vinifillos.repositories;

import io.github.vinifillos.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
