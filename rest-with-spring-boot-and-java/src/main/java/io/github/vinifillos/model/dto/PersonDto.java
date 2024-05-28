package io.github.vinifillos.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonPropertyOrder({"id", "address", "first_Name", "last_Name", "gender"})
public class PersonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    @JsonProperty("first_Name")
    private String firstName;
    @JsonProperty("last_Name")
    private String lastName;
    private String address;
    @JsonIgnore
    private String gender;

    public PersonDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonDto person = (PersonDto) o;
        return id.equals(person.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
