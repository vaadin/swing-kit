package org.vaadin.example;

import com.vaadin.example.IPerson;
import com.vaadin.example.IPersonProvider;
import com.vaadin.example.PersonsData;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.swingkit.server.Bridge;

@PageTitle("Person UI")
@Route(value = "person")
@Bridge
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class PersonView extends FormLayout implements IPersonProvider {
    private TextField firstName, lastName, jobName;
    private DatePicker birth;

    private Binder<IPerson> binder;

    public PersonView() {
        binder = new Binder<>();

        firstName = new TextField();
        firstName.setSizeFull();
        binder.forField(firstName).bind(IPerson::getFirstName, IPerson::setFirstName);
        lastName = new TextField();
        lastName.setSizeFull();
        binder.forField(lastName).bind(IPerson::getLastName, IPerson::setLastName);
        jobName = new TextField();
        jobName.setSizeFull();
        binder.forField(jobName).bind(IPerson::getJob, IPerson::setJob);
        birth = new DatePicker();
        birth.setSizeFull();
        binder.forField(birth).bind(IPerson::getBirthDate, IPerson::setBirthDate);

        addFormItem(firstName, "First Name");
        addFormItem(lastName, "Last Name");
        addFormItem(jobName, "Occupation");
        addFormItem(birth, "Birthdate");
    }

    @Override
    public void show(Long id) {
        binder.setBean(PersonsData.getData().get(id));
    }
}
