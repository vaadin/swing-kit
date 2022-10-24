package com.vaadin.example;


import java.io.Serializable;
import java.time.LocalDate;

public interface IPerson extends Serializable {
    void setFirstName(String firstName);

    void setLastName(String lastName);

    void setJob(String job);

    void setBirthDate(LocalDate birthDate);

    String getFirstName();

    String getLastName();

    String getJob();

    LocalDate getBirthDate();
}
