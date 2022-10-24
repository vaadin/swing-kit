package org.vaadin.example;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * Vaadin View with all the Swing application migrated. This view would replace
 * the Swing application on a final stage.
 * 
 * @author Vaadin Ltd.
 *
 */
@PageTitle("Swing Kit Demo")
@Route(value = "/")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@JsModule("@vaadin/vaadin-lumo-styles/presets/compact.js")
public class PersonFullView extends VerticalLayout {
	public PersonFullView() {
		setAlignItems(Alignment.CENTER);
		H2 title = new H2("Person Example Application");
		add(title);
		PersonsTableView table = new PersonsTableView();
		add(table);
		PersonView person = new PersonView();
		add(person);
		// SInce there is no more Swing code on the application we link the views using
		// regular Vaadin event/listener methods.
		table.attachToDetails(person);
	}
}
