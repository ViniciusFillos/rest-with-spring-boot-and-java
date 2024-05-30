package io.github.vinifillos.unitTests.services;

import io.github.vinifillos.exceptions.RequiredObjectIsNullException;
import io.github.vinifillos.model.Person;
import io.github.vinifillos.model.dto.PersonDto;
import io.github.vinifillos.repositories.PersonRepository;
import io.github.vinifillos.services.PersonService;
import io.github.vinifillos.unitTests.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    MockPerson input;

    @InjectMocks
    private PersonService service;

    @Mock
    PersonRepository personRepository;

    @BeforeEach
    void setUpMocks() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        List<Person> list = input.mockEntityList();
        when(personRepository.findAll()).thenReturn(list);

        var people = service.findAll();

        assertNotNull(people);
        assertEquals(14,people.size());

        var personZero = people.get(0);
        var personSeven = people.get(7);
        var personFourteen = people.get(13);

        assertNotNull(personZero);
        assertNotNull(personZero.getKey());
        assertNotNull(personZero.getLinks());
        assertTrue(personZero.toString().contains("links: [</api/person/v1/0>;rel=\"self\"]"));
        assertEquals("Addres Test0", personZero.getAddress());
        assertEquals("First Name Test0", personZero.getFirstName());
        assertEquals("Last Name Test0", personZero.getLastName());
        assertEquals("Male", personZero.getGender());

        assertNotNull(personSeven);
        assertNotNull(personSeven.getKey());
        assertNotNull(personSeven.getLinks());
        assertTrue(personSeven.toString().contains("links: [</api/person/v1/7>;rel=\"self\"]"));
        assertEquals("Addres Test7", personSeven.getAddress());
        assertEquals("First Name Test7", personSeven.getFirstName());
        assertEquals("Last Name Test7", personSeven.getLastName());
        assertEquals("Female", personSeven.getGender());

        assertNotNull(personFourteen);
        assertNotNull(personFourteen.getKey());
        assertNotNull(personFourteen.getLinks());
        assertTrue(personFourteen.toString().contains("links: [</api/person/v1/13>;rel=\"self\"]"));
        assertEquals("Addres Test13", personFourteen.getAddress());
        assertEquals("First Name Test13", personFourteen.getFirstName());
        assertEquals("Last Name Test13", personFourteen.getLastName());
        assertEquals("Female", personFourteen.getGender());
    }

    @Test
    void findById_WithValidData_ReturnsPerson() {
        Person entity = input.mockEntity(1);
        entity.setId(1L);
        when(personRepository.findById(anyLong())).thenReturn(Optional.of(entity));

        var result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void create_WithValidData_ReturnPerson() {
        Person persisted = input.mockEntity(1);
        persisted.setId(1L);
        PersonDto dto = input.mockDto(1);
        dto.setKey(1L);
        when(personRepository.save(any(Person.class))).thenReturn(persisted);

        var result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void create_WithNullPerson_ReturnException() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> service.create(null));
        String expectedMessage = "It's not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update_WithValidData_ReturnPerson() {
        Person entity = input.mockEntity(1);
        entity.setId(1L);
        PersonDto dto = input.mockDto(1);
        dto.setKey(1L);
        when(personRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(personRepository.save(entity)).thenReturn(entity);

        var result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void update_WithNullPerson_ReturnException() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> service.update(null));
        String expectedMessage = "It's not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete_WithValidData_DoNotReturnException() {
        Person entity = input.mockEntity(1);
        entity.setId(1L);
        when(personRepository.findById(anyLong())).thenReturn(Optional.of(entity));

        assertDoesNotThrow(() -> service.delete(1L));
    }
}