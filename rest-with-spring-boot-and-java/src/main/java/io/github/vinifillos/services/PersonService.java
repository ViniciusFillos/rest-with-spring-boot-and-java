package io.github.vinifillos.services;

import io.github.vinifillos.exceptions.ResourceNotFoundException;
import io.github.vinifillos.mapper.ModelMapper;
import io.github.vinifillos.model.Person;
import io.github.vinifillos.model.dto.PersonDto;
import io.github.vinifillos.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;
    private Logger logger = Logger.getLogger(PersonService.class.getName());

    public List<PersonDto> findAll() {
        logger.info("Finding all people!");

        return ModelMapper.parseListObjects(personRepository.findAll(), PersonDto.class);
    }

    public PersonDto findById(Long id) {
        logger.info("Finding one person!");

        var entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        return ModelMapper.parseObject(entity, PersonDto.class);
    }


    public PersonDto create(PersonDto person) {
        logger.info("Creating one person!");
        var entity = ModelMapper.parseObject(person, Person.class);
        var dto = ModelMapper.parseObject(personRepository.save(entity), PersonDto.class);
        return dto;
    }

    public PersonDto update(PersonDto person) {
        logger.info("Updating one person!");
        var entity = personRepository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());

        var dto = ModelMapper.parseObject(personRepository.save(entity), PersonDto.class);
        return dto;
    }

    public void delete(Long id) {
        var entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        personRepository.delete(entity);
    }
}
