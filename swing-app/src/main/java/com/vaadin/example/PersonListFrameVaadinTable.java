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

/**
 * Implementation of the step 1a of the guide where the user decides to migrate
 * first the table view.
 * 
 * @author Vaadin Ltd.
 *
 */
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
	}

	private void setupFrame() {
		setTitle("Swing Kit Demo");
		Panel mainPanel = new Panel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel title = new JLabel("Swing Kit Demo");
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		title.setFont(new Font("Arial", Font.PLAIN, 16));
		mainPanel.add(title);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JVaadinPanel tableView = null;
		try {
			// We create the JVaadinPanel that will contain the table listing the Person
			// entities using the builder and pointing the view to the Vaadin implementation
			// of the table.
			tableView = SwingVaadinClient.getBuilder().build("http://localhost:8080/person-table-view");
			tableView.setPreferredSize(new Dimension(400, 45));
			// To allow the Swing side to receive the Vaadin side event produced when a user
			// selects a Person on the table we add a Swing Kit VaadinEventListener. This
			// listener receives a VaadinSwingEvent event.
			tableView.addEventListener(PERSON_SHOW_EVENT, new VaadinEventListener() {
				@Override
				public void handleEvent(VaadinSwingEvent vaadinSwingEvent) {
					// On this demo we only have one type of event but we add this code as a proof
					// of concept that the user can filter the events to know how to handle them.
					if (vaadinSwingEvent.getType().equals(PERSON_SHOW_EVENT)) {
						Long selectedId = (Long) vaadinSwingEvent.getParams().get(PERSON_PARAMETER);
						// We call the consumer. On this case is a Swing method. 
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
