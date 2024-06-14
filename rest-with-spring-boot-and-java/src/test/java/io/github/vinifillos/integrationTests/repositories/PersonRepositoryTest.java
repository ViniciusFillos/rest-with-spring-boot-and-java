package io.github.vinifillos.integrationTests.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.vinifillos.configs.ConfigTest;
import io.github.vinifillos.integrationTests.dto.PersonDto;
import io.github.vinifillos.integrationTests.dto.wrappers.WrapperPersonDto;
import io.github.vinifillos.integrationTests.testContainers.AbstractIntegrationTest;
import io.github.vinifillos.model.Person;
import io.github.vinifillos.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    public PersonRepository personRepository;

    private static Person person;

    @BeforeAll
    public static void setup() {
        person = new Person();
    }

    @Test
    @Order(1)
    void testFindByName() {

        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "firstName"));
        person = personRepository.findPeopleByName("vini", pageable).getContent().getFirst();

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertNotNull(person.getEnabled());

        assertEquals(1, person.getId());

        assertEquals("Vinicius", person.getFirstName());
        assertEquals("Fillos", person.getLastName());
        assertEquals("Street Alfredo Kamisnki", person.getAddress());
        assertEquals("Male", person.getGender());
        assertTrue(person.getEnabled());
    }

    @Test
    @Order(2)
    void testDisablePerson() {

        personRepository.disablePerson(person.getId());
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "firstName"));
        person = personRepository.findPeopleByName("vini", pageable).getContent().getFirst();

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertNotNull(person.getEnabled());

        assertEquals(1, person.getId());

        assertEquals("Vinicius", person.getFirstName());
        assertEquals("Fillos", person.getLastName());
        assertEquals("Street Alfredo Kamisnki", person.getAddress());
        assertEquals("Male", person.getGender());
        assertFalse(person.getEnabled());
    }
}
