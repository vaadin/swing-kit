package com.vaadin.example;

import com.vaadin.example.views.IPersonView;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

public class PersonDetailsPanel extends JPanel implements IPersonView {
    private TextField txtFirstName, txtLastName, txtJob;

    private UtilDateModel birthDateModel;

    public PersonDetailsPanel() {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        txtFirstName = new TextField();
        Label label = new Label("First Name:");
        add(label, labelCons(0));
        add(txtFirstName, txtCons(0));

        txtLastName = new TextField();
        label = new Label("Last Name:");
        add(label, labelCons(1));
        add(txtLastName, txtCons(1));

        txtJob = new TextField();
        label = new Label("Occupation:");
        add(label, labelCons(2));
        add(txtJob, txtCons(2));

        label = new Label("Birthdate:");
        add(label, labelCons(3));
        Properties p = new Properties();
        birthDateModel = new UtilDateModel();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(birthDateModel, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        add(datePicker, txtCons(3));

        this.setBorder(BorderFactory.createLineBorder(Color.black));

        this.setMaximumSize(new Dimension(400, 200));
    }

    private GridBagConstraints labelCons(int index) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = index;
        gbc.fill  = GridBagConstraints.BOTH;
        gbc.weightx = 0.1f;
        gbc.insets = new Insets(3, 3, 3, 3);
        return gbc;
    }

    private GridBagConstraints txtCons(int index) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = index;
        gbc.fill  = GridBagConstraints.BOTH;
        gbc.weightx = 0.9f;
        gbc.insets = new Insets(3, 3, 3, 3);
        return gbc;
    }

    @Override
    public void show(Long id) {
        IPerson person = PersonsData.getData().get(id);
        txtFirstName.setText(person.getFirstName());
        txtLastName.setText(person.getLastName());
        txtJob.setText(person.getJob());
        birthDateModel.setValue(Date.from(person.getBirthDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
}
