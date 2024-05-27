package io.github.vinifillos.unitTests.mocks;

import io.github.vinifillos.model.Person;
import io.github.vinifillos.model.dtoV1.PersonDtoV1;

import java.util.ArrayList;
import java.util.List;



public class MockPerson {


    public Person mockEntity() {
        return mockEntity(0);
    }
    
    public PersonDtoV1 mockVO() {
        return mockVO(0);
    }
    
    public List<Person> mockEntityList() {
        List<Person> persons = new ArrayList<Person>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i));
        }
        return persons;
    }

    public List<PersonDtoV1> mockVOList() {
        List<PersonDtoV1> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockVO(i));
        }
        return persons;
    }
    
    public Person mockEntity(Integer number) {
        Person person = new Person();
        person.setAddress("Addres Test" + number);
        person.setFirstName("First Name Test" + number);
        person.setGender(((number % 2)==0) ? "Male" : "Female");
        person.setId(number.longValue());
        person.setLastName("Last Name Test" + number);
        return person;
    }

    public PersonDtoV1 mockVO(Integer number) {
        PersonDtoV1 person = new PersonDtoV1();
        person.setAddress("Addres Test" + number);
        person.setFirstName("First Name Test" + number);
        person.setGender(((number % 2)==0) ? "Male" : "Female");
        person.setId(number.longValue());
        person.setLastName("Last Name Test" + number);
        return person;
    }

}
