package com.vaadin.example;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.stream.Collectors;

public class PersonTable extends JPanel {

    ListSelectionModel selectModel;

    public PersonTable() {

        String[][] data = PersonsData.getData().values().stream().map( p ->
                new String[] {
                        p.getFirstName(),
                        p.getLastName(),
                        p.getJob(),
                        p.getBirthDate().toString()
                }).collect(Collectors.toList()).toArray(new String[0][0]);

        String column[] = {"First Name", "Last Name", "Occupation", "Birthdate"};

        JTable jt = new JTable(data, column);
        jt.setPreferredSize(new Dimension(400,100));
        jt.setPreferredScrollableViewportSize(jt.getPreferredSize());

        selectModel = jt.getSelectionModel();
        selectModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(jt);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
        jt.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        add(scrollPane);
    }

    public void addListSelectionListener(ListSelectionListener listener) {
        selectModel.addListSelectionListener(listener);
    }
}
