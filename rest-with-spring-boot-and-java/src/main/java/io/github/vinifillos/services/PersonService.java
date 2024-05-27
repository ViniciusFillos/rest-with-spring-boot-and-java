package io.github.vinifillos.services;

import io.github.vinifillos.exceptions.ResourceNotFoundException;
import io.github.vinifillos.mapper.ModelMapper;
import io.github.vinifillos.mapper.custom.PersonMapper;
import io.github.vinifillos.model.Person;
import io.github.vinifillos.model.dtoV1.PersonDtoV1;
import io.github.vinifillos.model.dtoV2.PersonDtoV2;
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
    @Autowired
    PersonMapper personMapper;

    public List<PersonDtoV1> findAll() {
        logger.info("Finding all people!");

        return ModelMapper.parseListObjects(personRepository.findAll(), PersonDtoV1.class);
    }

    public PersonDtoV1 findById(Long id) {
        logger.info("Finding one person!");

        var entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        return ModelMapper.parseObject(entity, PersonDtoV1.class);
    }


    public PersonDtoV1 create(PersonDtoV1 person) {
        logger.info("Creating one person!");
        var entity = ModelMapper.parseObject(person, Person.class);
        var dto = ModelMapper.parseObject(personRepository.save(entity), PersonDtoV1.class);
        return dto;
    }

    public PersonDtoV2 createV2(PersonDtoV2 person) {
        logger.info("Creating one person with V2!");
        var entity = personMapper.convertDtoToEntity(person);
        var dto = personMapper.convertEntityToDto(personRepository.save(entity));
        return dto;
    }

    public PersonDtoV1 update(PersonDtoV1 person) {
        logger.info("Updating one person!");
        var entity = personRepository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());

        var dto = ModelMapper.parseObject(personRepository.save(entity), PersonDtoV1.class);
        return dto;
    }

    public void delete(Long id) {
        var entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        personRepository.delete(entity);
    }
}
