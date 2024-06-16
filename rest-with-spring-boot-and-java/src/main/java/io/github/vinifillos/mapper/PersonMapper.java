package io.github.vinifillos.mapper;

import io.github.vinifillos.model.Person;
import io.github.vinifillos.model.dto.PersonDto;

import java.util.ArrayList;
import java.util.List;

public class PersonMapper {

    private static org.modelmapper.ModelMapper mapper = new org.modelmapper.ModelMapper();

    public static Person fromDtoToPerson(PersonDto dto) {
        Person person = mapper.map(dto, Person.class);
        person.setId(dto.getKey());
        return person;
    }

    public static PersonDto fromPersonToDto(Person person) {
        PersonDto dto = mapper.map(person, PersonDto.class);
        dto.setKey(person.getId());
        return dto;
    }

    public static List<Person> parseListDtoToPerson(List<PersonDto> dtosList) {
        List<Person> personList = new ArrayList<>();
        for (PersonDto dto : dtosList) {
            var entity = mapper.map(dto, Person.class);
            entity.setId(dto.getKey());
            personList.add(entity);
        }
        return personList;
    }

    public static List<PersonDto> parseListPersonToDto(List<Person> personList) {
        List<PersonDto> dtosList = new ArrayList<>();
        for (Person person : personList) {
            var entity = mapper.map(person, PersonDto.class);
            entity.setKey(person.getId());
            dtosList.add(entity);
        }
        return dtosList;
    }
}