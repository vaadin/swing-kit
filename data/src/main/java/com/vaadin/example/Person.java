package com.vaadin.example;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;

/**
 * Implementation of the entity that will be shared between views of the application.
 * 
 * @author Vaadin Ltd.
 *
 */
public class Person implements IPerson, Serializable {

    private String firstName, lastName, job;
    private LocalDate birthDate;

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

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
