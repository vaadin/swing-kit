package com.vaadin.example;

import com.vaadin.swingkit.client.JVaadinPanel;
import com.vaadin.swingkit.client.SwingVaadinClient;
import com.vaadin.swingkit.client.SwingVaadinException;
import com.vaadin.swingkit.client.VaadinEventListener;
import com.vaadin.swingkit.core.VaadinSwingEvent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

import static com.vaadin.example.Constants.PERSON_PARAMETER;
import static com.vaadin.example.Constants.PERSON_SHOW_EVENT;

public class PersonListFrameVaadinTable extends JFrame {

    private JVaadinPanel tableView;
    private PersonDetailsPanel personDetailView;

    public static void main(String[] a) {
        PersonListFrameVaadinTable frame = new PersonListFrameVaadinTable();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public PersonListFrameVaadinTable() {
        setupFrame();
        attachListeners();
    }

    private void attachListeners() {
        /*
        tableView.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                Long selectedId = (long) ((ListSelectionModel)e.getSource()).getSelectedIndices()[0] + 1;
                personDetailView.show(selectedId);
            }
        });
         */
    }

    private void setupFrame() {
        setTitle("Swing Kit Demo");
        Panel mainPanel = new Panel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel title = new JLabel("Person List Application");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial",Font.PLAIN,16));
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JVaadinPanel tableView = null;
        try {
            tableView = SwingVaadinClient.getBuilder().build("http://localhost:8080/person-table-view");
            tableView.setPreferredSize(new Dimension(400, 45));
            tableView.addEventListener(PERSON_SHOW_EVENT, new VaadinEventListener() {
                @Override
                public void handleEvent(VaadinSwingEvent vaadinSwingEvent) {
					if (vaadinSwingEvent.getType().equals(PERSON_SHOW_EVENT)) {
						Long selectedId = (Long) vaadinSwingEvent.getParams().get(PERSON_PARAMETER);
						personDetailView.show(selectedId);
					}
                }
            });
        } catch (SwingVaadinException e) {
            throw new RuntimeException(e);
        }
        mainPanel.add(tableView);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        personDetailView = new PersonDetailsPanel();
        mainPanel.add(personDetailView);
        mainPanel.setPreferredSize(new Dimension(400, 200));

        mainPanel.add(Box.createVerticalGlue());

        setResizable(false);
        add(mainPanel);
        setSize(450, 420);
    }
}
