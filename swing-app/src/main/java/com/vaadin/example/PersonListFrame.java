package com.vaadin.example;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class PersonListFrame extends JFrame {

    private PersonTable tableView;
    private PersonDetailsPanel personDetailView;

    public static void main(String[] a) {
        PersonListFrame frame = new PersonListFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public PersonListFrame() {
        setupFrame();
        attachListeners();
    }

    private void attachListeners() {
        tableView.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                Long selectedId = (long) ((ListSelectionModel)e.getSource()).getSelectedIndices()[0] + 1;
                personDetailView.show(selectedId);
            }
        });
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

        tableView = new PersonTable();
        mainPanel.add(tableView);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        personDetailView = new PersonDetailsPanel();
        mainPanel.add(personDetailView);
        mainPanel.setPreferredSize(new Dimension(400, 200));

        mainPanel.add(Box.createVerticalGlue());

        setResizable(false);
        add(mainPanel);
        setSize(450, 400);
    }
}
