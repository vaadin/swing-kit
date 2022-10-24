package com.vaadin.example;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;

public class Person implements IPerson, Serializable {

    public static Person createDefault() {
        Person p = new Person();
        p.setFirstName("Napoleon");
        p.setLastName("Bonaparte");
        p.setJob("Emperor of the French");
        p.setBirthDate(LocalDate.of(1789, Calendar.AUGUST, 15));
        return p;
    }

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
