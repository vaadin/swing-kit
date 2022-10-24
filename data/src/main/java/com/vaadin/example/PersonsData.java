package com.vaadin.example;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

public class PersonsData {
    static {
        BiMap<Long,IPerson> investigators = HashBiMap.create();

        IPerson marie = new Person();
        marie.setFirstName("Marie");
        marie.setLastName("Lambeau");
        marie.setJob("Entertainer");
        marie.setBirthDate(LocalDate.of(1892,12,06));
        investigators.put(1L, marie);

        IPerson jack = new Person();
        jack.setFirstName("Jack");
        jack.setLastName("Monterey");
        jack.setJob("Archeologist");
        jack.setBirthDate(LocalDate.of(1894,07,19));
        investigators.put(2L, jack);

        IPerson rita = new Person();
        rita.setFirstName("Rita");
        rita.setLastName("Young");
        rita.setJob("Athlete");
        rita.setBirthDate(LocalDate.of(1895,11,15));
        investigators.put(3L, rita);

        IPerson dexter = new Person();
        dexter.setFirstName("Dexter");
        dexter.setLastName("Drake");
        dexter.setJob("Magician");
        dexter.setBirthDate(LocalDate.of(1896,01,06));
        investigators.put(4L, dexter);

        IPerson carolyn = new Person();
        carolyn.setFirstName("Carolyn");
        carolyn.setLastName("Fern");
        carolyn.setJob("Psychologist");
        carolyn.setBirthDate(LocalDate.of(1896,01,11));
        investigators.put(5L, carolyn);

        IPerson daisy = new Person();
        daisy.setFirstName("Daisy");
        daisy.setLastName("Walker");
        daisy.setJob("Librarian");
        daisy.setBirthDate(LocalDate.of(1897,07,24));
        investigators.put(6L, daisy);

        data = investigators;
    }

    private static final BiMap<Long,IPerson> data;

    public static BiMap<Long,IPerson> getData() {
        return data;
    }

}
