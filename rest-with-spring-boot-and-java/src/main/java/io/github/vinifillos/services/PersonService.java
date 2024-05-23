package io.github.vinifillos.services;

import io.github.vinifillos.model.Person;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonService {

    private static final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonService.class.getName());

    public Person findById(String id) {

        logger.info("Finding one person!");
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Vinicius");
        person.setLastName("Fillos");
        person.setAddress("Irati - Paraná - Brasil");
        person.setGender("Male");
        return person;
    }
}
