package io.github.vinifillos.controllers;

import io.github.vinifillos.model.dtoV1.PersonDtoV1;
import io.github.vinifillos.model.dtoV2.PersonDtoV2;
import io.github.vinifillos.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonDtoV1 findById(@PathVariable(value = "id") Long id) {
        return personService.findById(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PersonDtoV1> findAll() {
        return personService.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonDtoV1 create(@RequestBody PersonDtoV1 person) {
        return personService.create(person);
    }

    @PostMapping(value = "/v2", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonDtoV2 createV2(@RequestBody PersonDtoV2 person) {
        return personService.createV2(person);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonDtoV1 update(@RequestBody PersonDtoV1 person) {
        return personService.update(person);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id") Long id) {
        personService.delete(id);
    }
}
