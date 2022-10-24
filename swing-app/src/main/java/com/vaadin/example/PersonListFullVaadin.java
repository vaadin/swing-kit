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

public class PersonListFullVaadin extends JFrame {

    private PersonTable tableView;
    private IPersonProvider personDetailsProvider;

    public static void main(String[] a) {
        PersonListFullVaadin frame = new PersonListFullVaadin();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public PersonListFullVaadin() {
        setupFrame();
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
            tableView.setPreferredSize(new Dimension(400, 50));
            tableView.addEventListener(PERSON_SHOW_EVENT, new VaadinEventListener() {
                @Override
                public void handleEvent(VaadinSwingEvent vaadinSwingEvent) {
					if (vaadinSwingEvent.getType().equals(PERSON_SHOW_EVENT)) {
						Long selectedId = (Long) vaadinSwingEvent.getParams().get(PERSON_PARAMETER);
						personDetailsProvider.show(selectedId);
					}
                }
            });
        } catch (SwingVaadinException e) {
            throw new RuntimeException(e);
        }
        mainPanel.add(tableView);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JVaadinPanel personDetails = null;
        try {
            personDetails = SwingVaadinClient.getBuilder().build("http://localhost:8080/person");
            personDetailsProvider = personDetails.as(IPersonProvider.class);
        } catch (SwingVaadinException e) {
            throw new RuntimeException(e);
        }
        mainPanel.add(personDetails);
        mainPanel.setPreferredSize(new Dimension(400, 200));

        mainPanel.add(Box.createVerticalGlue());

        setResizable(false);
        add(mainPanel);
        setSize(450, 480);
    }
}