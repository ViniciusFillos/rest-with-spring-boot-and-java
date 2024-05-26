package io.github.vinifillos.services;

import io.github.vinifillos.exceptions.ResourceNotFoundException;
import io.github.vinifillos.model.Person;
import io.github.vinifillos.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;
    private Logger logger = Logger.getLogger(PersonService.class.getName());

    public List<Person> findAll() {
        logger.info("Finding all people!");

        return personRepository.findAll();
    }

    public Person findById(Long id) {
        logger.info("Finding one person!");

        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
    }


    public Person create(Person person) {
        logger.info("Creating one person!");
        return personRepository.save(person);
    }

    public Person update(Person person) {
        logger.info("Updating one person!");
        Person entity = findById(person.getId());

        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());

        return personRepository.save(entity);
    }

    public void delete(Long id) {
        var entity = findById(id);
        personRepository.delete(entity);
    }
}
