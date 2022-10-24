package com.vaadin.example;

import com.vaadin.swingkit.client.JVaadinPanel;
import com.vaadin.swingkit.client.SwingVaadinClient;
import com.vaadin.swingkit.client.SwingVaadinException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

/**
 * Implementation of the step 1b of the guide where the user decides to migrate
 * first the detailed information form view.
 * 
 * @author Vaadin Ltd.
 *
 */
public class PersonListFrameVaadinDetails extends JFrame {

	private PersonTable tableView;
	private IPersonProvider personDetailsProvider;

	public static void main(String[] a) {
		PersonListFrameVaadinDetails frame = new PersonListFrameVaadinDetails();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public PersonListFrameVaadinDetails() {
		setupFrame();
		attachListeners();
	}

	/**
	 * JTable listener that will deliver the Swing event to the Vaadin view consumer
	 * (an IPersonProvider implementation)
	 */
	private void attachListeners() {
		tableView.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				Long selectedId = (long) ((ListSelectionModel) e.getSource()).getSelectedIndices()[0] + 1;
				personDetailsProvider.show(selectedId);
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
		title.setFont(new Font("Arial", Font.PLAIN, 16));
		mainPanel.add(title);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		tableView = new PersonTable();
		mainPanel.add(tableView);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JVaadinPanel personDetails = null;
		try {
			// We create the JVaadinPanel that will contain the detailed information form
			// using the builder and pointing the view to the Vaadin implementation of the
			// form.
			personDetails = SwingVaadinClient.getBuilder().build("http://localhost:8080/person");
			// To allow the Swing side to call the Vaadin side using a SwingVaadinCallable
			// interface we cast the JVaadinPanel as a IPersonProvider implementation that
			// will be called when a item is selected on the JTable.
			personDetailsProvider = personDetails.as(IPersonProvider.class);
		} catch (SwingVaadinException e) {
			throw new RuntimeException(e);
		}
		mainPanel.add(personDetails);
		mainPanel.setPreferredSize(new Dimension(400, 200));

		mainPanel.add(Box.createVerticalGlue());

		setResizable(false);
		add(mainPanel);
		setSize(450, 400);
	}
}
