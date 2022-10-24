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
        grid.addThemeVariants(SwingVaadinServer.isSwingRendered() ? GridVariant.LUMO_COMPACT : GridVariant.LUMO_ROW_STRIPES);
        grid.setVerticalScrollingEnabled(false);
        grid.setAllRowsVisible(true);
        if (SwingVaadinServer.isSwingRendered()) {
            grid.addSelectionListener(selection -> {
                if (selection.getFirstSelectedItem().isPresent()) {
                    Long id = PersonsData.getData().inverse().get(selection.getFirstSelectedItem().get());
                    System.out.println("ID "+id+" "+selection.getFirstSelectedItem());
                    if (id != null) {
                        HashMap<String, Serializable> params = new HashMap<>();
                        params.put(PERSON_PARAMETER, id);
                        VaadinSwingEvent showEvent = new VaadinSwingEvent(PERSON_SHOW_EVENT, params);
                        EventEmitterFactory.newEventEmitter().emit(showEvent);
                    }
                }
            });
        }
        add(grid);
    }

    public void attachToDetails(PersonView view) {
        grid.addSelectionListener(selection -> {
            if (selection.getFirstSelectedItem().isPresent()) {
                Long id = PersonsData.getData().inverse().get(selection.getFirstSelectedItem().get());
                System.out.println("ID "+id+" "+selection.getFirstSelectedItem());
                if (id != null) {
                    view.show(id);
                }
            }
        });
    }
}
