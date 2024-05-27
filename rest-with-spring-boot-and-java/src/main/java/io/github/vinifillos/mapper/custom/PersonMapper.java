package io.github.vinifillos.mapper.custom;

import io.github.vinifillos.model.Person;
import io.github.vinifillos.model.dtoV2.PersonDtoV2;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonMapper {

    public PersonDtoV2 convertEntityToDto(Person person) {
        PersonDtoV2 dto = new PersonDtoV2();
        dto.setId(person.getId());
        dto.setAddress(person.getAddress());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setGender(person.getGender());
        dto.setBirthDay(new Date());
        return dto;
    }

    public Person convertDtoToEntity(PersonDtoV2 person) {
        Person entity = new Person();
        entity.setId(person.getId());
        entity.setAddress(person.getAddress());
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setGender(person.getGender());
        //entity.setBirthDay(new Date());
        return entity;
    }
}
