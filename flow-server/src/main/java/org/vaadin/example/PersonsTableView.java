package org.vaadin.example;

import com.vaadin.example.IPerson;
import com.vaadin.example.Person;
import com.vaadin.example.PersonsData;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.swingkit.core.VaadinSwingEvent;
import com.vaadin.swingkit.server.Bridge;
import com.vaadin.swingkit.server.EventEmitter;
import com.vaadin.swingkit.server.EventEmitterFactory;
import com.vaadin.swingkit.server.SwingVaadinServer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

import static com.vaadin.example.Constants.PERSON_PARAMETER;
import static com.vaadin.example.Constants.PERSON_SHOW_EVENT;

/**
 * Vaadin view containing a Grid to list the Person entities of the demo. This
 * grid will emit an event when an item is selected and this event will be
 * delivered:<br>
 * 
 * <ul>
 * <li>To the JVaadinPanel hosting the view on an intermediate phase of the
 * migration when the this view is rendered by Swing Kit and the detailed info
 * panel is implemented in the Swing side.</li>
 * <li>To another Vaadin View containing the detailed info panel when we reach
 * the final stage of the migration where we remove all Swing code.</li>
 * </ul>
 * 
 * @author Vaadin Ltd.
 *
 */
@PageTitle("Person Table View")
@Route(value = "person-table-view")
@Bridge
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@JsModule("@vaadin/vaadin-lumo-styles/presets/compact.js")
public class PersonsTableView extends VerticalLayout {
	private Grid<IPerson> grid;

	public PersonsTableView() {
		grid = new Grid<>(IPerson.class, false);
		grid.addColumn(IPerson::getFirstName).setHeader("First name");
		grid.addColumn(IPerson::getLastName).setHeader("Last name");
		grid.addColumn(IPerson::getJob).setHeader("Occupation");
		grid.addColumn(IPerson::getBirthDate).setHeader("Birth Date");

		grid.setItems(PersonsData.getData().values());
		grid.addThemeVariants(
				SwingVaadinServer.isSwingRendered() ? GridVariant.LUMO_COMPACT : GridVariant.LUMO_ROW_STRIPES);
		grid.setVerticalScrollingEnabled(false);
		grid.setAllRowsVisible(true);
		// When this view is rendered in a Swing Application using Swing kit we are on a
		// intermediate step of the migration and we need to communicata the events
		// produced by the Grid item selection to the Swing side.
		if (SwingVaadinServer.isSwingRendered()) {
			grid.addSelectionListener(selection -> {
				if (selection.getFirstSelectedItem().isPresent()) {
					Long id = PersonsData.getData().inverse().get(selection.getFirstSelectedItem().get());
					System.out.println("ID " + id + " " + selection.getFirstSelectedItem());
					if (id != null) {
						// Creation of the VaadinSwingEvent. We create the parameters to include on the
						// event, the Person identifier on this case.
						HashMap<String, Serializable> params = new HashMap<>();
						params.put(PERSON_PARAMETER, id);
						// We define the event giving to it a unique Strin identifier so the event can
						// be filtered conveniently on the JVaadinPanel of the Swing Side. On this demo
						// there is only one event so this filtering is not required but it is done on
						// the Swing Side as a proof of concept
						VaadinSwingEvent showEvent = new VaadinSwingEvent(PERSON_SHOW_EVENT, params);
						// When the event is ready we use the EventEmitterFactory to emit the event that
						// will be delivered to the JVaadinPanel containing the view.
						EventEmitterFactory.newEventEmitter().emit(showEvent);
					}
				}
			});
		}
		add(grid);
	}

	/**
	 * This method is only used on the final version of the migration when we remove
	 * definitely the Swing code and the interaction between Grid and Form views is
	 * completely managed by Vaadin
	 * 
	 * @param view The detailed info panel
	 */
	public void attachToDetails(PersonView view) {
		grid.addSelectionListener(selection -> {
			if (selection.getFirstSelectedItem().isPresent()) {
				Long id = PersonsData.getData().inverse().get(selection.getFirstSelectedItem().get());
				System.out.println("ID " + id + " " + selection.getFirstSelectedItem());
				if (id != null) {
					view.show(id);
				}
			}
		});
	}
}
