package io.github.vinifillos.services;

import io.github.vinifillos.controllers.PersonController;
import io.github.vinifillos.exceptions.RequiredObjectIsNullException;
import io.github.vinifillos.exceptions.ResourceNotFoundException;
import io.github.vinifillos.mapper.PersonMapper;
import io.github.vinifillos.model.dto.PersonDto;
import io.github.vinifillos.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final Logger logger = Logger.getLogger(PersonService.class.getName());
    final PagedResourcesAssembler<PersonDto> assembler;

    public PagedModel<EntityModel<PersonDto>> findAll(Pageable pageable) {
        logger.info("Finding all people!");

        var personPage = personRepository.findAll(pageable);
        var personDtosPage = personPage.map(PersonMapper::fromPersonToDto);
        personDtosPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
        Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(personDtosPage, link);
    }

    public PagedModel<EntityModel<PersonDto>> findPeopleByName(String firstName, Pageable pageable) {
        logger.info("Finding all people!");

        var personPage = personRepository.findPeopleByName(firstName, pageable);
        var personDtosPage = personPage.map(PersonMapper::fromPersonToDto);
        personDtosPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
        Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(personDtosPage, link);
    }

    public PersonDto findById(Long id) {
        logger.info("Finding one person!");

        var entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var dto = PersonMapper.fromPersonToDto(entity);
        dto.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return dto;
    }

    public PersonDto create(PersonDto person) {
        if (person == null) throw new RequiredObjectIsNullException();
        logger.info("Creating one person!");
        var entity = PersonMapper.fromDtoToPerson(person);
        var dto = PersonMapper.fromPersonToDto(personRepository.save(entity));
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getKey())).withSelfRel());
        return dto;
    }

    public PersonDto update(PersonDto person) {
        if (person == null) throw new RequiredObjectIsNullException();
        logger.info("Updating one person!");
        var entity = personRepository.findById(person.getKey())
                .orElseThrow(ResourceNotFoundException::new);

        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());

        var dto = PersonMapper.fromPersonToDto(personRepository.save(entity));
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getKey())).withSelfRel());
        return dto;
    }

    @Transactional
    public PersonDto disablePerson(Long id) {
        logger.info("Disabling one person!");
        personRepository.disablePerson(id);
        var entity = personRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        var dto = PersonMapper.fromPersonToDto(entity);
        dto.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return dto;
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");

        var entity = personRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        personRepository.delete(entity);
    }
}
