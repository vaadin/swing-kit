# Swing Kit Example

This demo contains a full example of a phased migration of a simple Swing App to Vaadin 23 using the Swing Kit as a migration helper. 

First compile the modules with
```
mvn clean install
```

## Run the project

This project includes two applications. A valid **Vaadin PRO license is required** to run this Swing Kit demo.

On one terminal, run Vaadin application:
```
cd flow-server
mvn
```
You can check the different pieces of this demo.

- Table View migration page at `http://localhost:8080/person-table-view`
- Detailed Info migration page at `http://localhost:8080/person`
- Final migration page at `http://localhost:8080/`

On a second terminal, run Swing application:
```
cd swing-app/target

# Windows and MacOs
- Original Swing App: java -cp ./target/swing-app-1.0-SNAPSHOT.jar com.vaadin.example.PersonListFrame
- Table migrated to Vaadin: java -cp ./target/swing-app-1.0-SNAPSHOT.jar com.vaadin.example.PersonListFrameVaadinTable
- Detailed info form migrated to Vaadin: java -cp ./target/swing-app-1.0-SNAPSHOT.jar com.vaadin.example.PersonListFrameVaadinDetails
- Full app migrated to Vaadin: java -cp ./target/swing-app-1.0-SNAPSHOT.jar com.vaadin.example.PersonListFullVaadin

# For MacOS and Java >= 17 add these parameters to the command (if you get exception related to sun.awt package)
java --add-opens java.desktop/sun.awt=ALL-UNNAMED --add-opens java.desktop/sun.lwawt=ALL-UNNAMED --add-opens java.desktop/sun.lwawt.macosx=ALL-UNNAMED -cp ./target/swing-app-1.0-SNAPSHOT.jar com.vaadin.example.PersonListFrame

```
## Development

You can import the project in your IDE as a maven project using the root directory.
This will include a 3 multimodule project.
* flow-server (contains Vaadin Views)
* swing-app (contains Swing Frames)
* data (should contain everything related to your application that is not Vaadin or Swing dependent)

## Demo

### Goal: Automating the Swing-to-Vaadin journey

The purpose of the Swing Kit is to reduce risks and time required in a big-bang migration from Swing to Vaadin, giving the option of a phased process. In this guide we will show the process of migrating one view at the time, and render them inside your Swing Application.

Swing Kit contains an embedded browser with additional Vaadin-integration features. It allows to render Vaadin views inside Swing Panels and helps to manage Events and Calls that can be shared between the different view technologies.

#### Basic Concepts

- **JVaadinPanel:** The extension of a Swing JPanel to be used in Swing applications that contains an embedded browser where Vaadin views will be displayed.  
- **SwingVaadinCallable:** The interface that makes a Vaadin view callable from the JVaadinPanel, that is from the Swing side.
- **VaadinSwingEvent:** The Event class that allows the user to send asynchronous events from the Vaadin view to the Swing side. 
- **Phased Migration:** The Swing Kit migrations approach lets the user to migrate a Swing application view by view. 

**Initial State**

The user has a Swing application to be migrated to Vaadin that contains a number of views.

![InitialState](https://user-images.githubusercontent.com/106953874/197408476-96b58a80-b8f6-44a9-9e89-bf9c75081190.png)

**Work In Progress State**

Views are migrated one by one with the level of abstraction and complexity that the user desires.

![MiddleState](https://user-images.githubusercontent.com/106953874/197408479-9e9fcfb3-1571-4905-968a-5166dba83124.png)

**Final State**

After all views are migrated into Vaadin, the user can use the application with the browser: migration ends.

![FinalState](https://user-images.githubusercontent.com/106953874/197408486-6031a607-f976-4718-b10a-5409e52703d1.png)

### Step-by-Step guide

**QuickTips:**

One of the advantages of the Vaadin Swing Kit is that the user can develop the view and test on a regular browser all functionalities except of course the ones that involves communications between Swing and Vaadin sides. So the user should start always implementing the Vaadin view that will replace the Swing JPanel. To ease the development the Swing Kit provides an API to detect if the view is been rendered on a regular browser or on a JVaadinPanel. This way the user can run the same code without having a version for regular browsers and another version for JVaadinPanel. 

```java
SwingVaadinServer.isSwingRendered()
```

Swing Kit provides an API to the user to communicate between Swing side and Vaadin Side and viceversa. Entities that will be shared between sides have to be accesible from both modules the Vaadin module and the Swing module. So it is strongly recommended that these entities are defined on third separated module. 

In Swing Kit API you will find classes with SwingVaadin and VaadinSwing prefixes. This naming helps to understand the direction of the communication that the class provides, Swing to Vaadin or Vaadin to Swing.

#### The data and calls

In this demo the Data module is provided with the definition of data entities and interfaces for calling Vaadin from Swing side.

**IPersonProvider:** This Interface callable from Swing side and implemented on the Vaadin side will show some information of a user identified by an ID.

```java 
public interface IPersonProvider extends SwingVaadinCallable {
    void show(Long id);
}
```

**IPerson:** The IPerson interface that represents a Person Entity.

```java
public interface IPerson extends Serializable {
    void setFirstName(String firstName);

    void setLastName(String lastName);

    void setJob(String job);

    void setBirthDate(LocalDate birthDate);

    String getFirstName();

    String getLastName();

    String getJob();

    LocalDate getBirthDate();
}
```

**IPersonView:** The IPersonView interface represents a visual element that visualize the detailed information of a person.

```java
public interface IPersonView {
    void show(Long id);
}
```

#### Step 0: The Swing app

For this demo we have a Swing application with a main JFrame containing two panels. The top panel contains a table with a list of people and the bottom panel contains a form where after clicking on any item of the table, detailed information of the person is displayed. 

![Swing Application](https://user-images.githubusercontent.com/106953874/197408830-c72a3b96-58e5-4d24-9186-dec0cf71ff0c.png)

**The table**

The Swing Table lists a collection of IPerson (link to PersonTable)

```java 
        String[][] data = PersonsData.getData().values().stream().map( p ->
                new String[] {
                        p.getFirstName(),
                        p.getLastName(),
                        p.getJob(),
                        p.getBirthDate().toString()
                }).collect(Collectors.toList()).toArray(new String[0][0]);

        String column[] = {"First Name", "Last Name", "Occupation", "Birthdate"};

        JTable jt = new JTable(data, column);
```

The Selection behaviour of the table is defined as a single selection behaviour. 

```java
selectModel = jt.getSelectionModel();
        selectModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

```

**The detailed information panel**

The form displays the detailed information of a IPerson

```java
   @Override
    public void show(Long id) {
        IPerson person = PersonsData.getData().get(id);
        txtFirstName.setText(person.getFirstName());
        txtLastName.setText(person.getLastName());
        txtJob.setText(person.getJob());
        birthDateModel.setValue(Date.from(person.getBirthDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
```

**The Main app**

The main app is a JFrame containing the Table and the Form and registering a ListSelectionListener from the table to the form. 
```java
    private void attachListeners() {
        tableView.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                Long selectedId = (long) ((ListSelectionModel)e.getSource()).getSelectedIndices()[0] + 1;
                personDetailView.show(selectedId);
            }
        });
    }
```

The Vaadin Swing Kit allows the user to migrate **view by view** so in this Demo the user could choose to migrate first the table view or the form view. 


#### Step 1: Choose a first view migration

Two main points that the user should have in mind when choosing how to start the migration are how the application can be separated on different views and how these views communicate between them. Of course a view is an abstract concept and the user could decide that the minimum view contains the table and the form and migrate everything at a time but the goal of this guide is to show how to implement a phased migration. On a real world case the Swing Kit allows the migration teams to decide how to split in views the application with any criteria of abstraction and according to their available efforts and schedule. Flexibility is a skill of Swing Kit and gives the user the posibility to split the migration at the lowest level (one view per Swing component for example) where the user will need to implement more communication methods between Vaadin views and Swing components or split the migration at a higher level (a table and a form is the same view for example) saving communication methods but spending more time till a Vaadin view can replaced a Swing Panel. 

So in this demo we decide to go to the lowest level of abstraction and then we have two views:

**Table view**

Is a table with a single selection event that is handled showing on the form the detailed person information. This means that if we migrate first this view we will need a communication from the Vaadin Grid to the Swing application. For this mission Swing Kit provides the VaadinSwingEvent that is an asynchronous event that the user can produce from a Vaadin view to be consumed by a Swing component. See further details on Step 1a: Migrate first the table.

**Detailed info view**

Is a form where the detailed information of the person selected on the table will be displayed. This means that if we migrate first this view we will need a communication from the Swing Jtable component to the Vaadin view. For this mission Swing Kit provides SwingVaadinCallable interface. The Vaadin view that will be called from Swing has to implement this interface. See further details on Step 1b: Migrate first the detailed info panel. 

##### Step 1a: Migrate first the table

In this case the user decides to migrate first the top panel containing the table. Then we implement a Vaadin Grid ready to list IPerson data. 

```java
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
        add(grid);
    }
```

So far this is just a regular Vaadin view with a grid than can already be displayed on a browser. 

![Vaadin Grid view on a browser](https://user-images.githubusercontent.com/106953874/197409035-b8fc0f08-5769-4129-ba03-a3244a66488b.png)


We continue adding the communication capability and using the Swing Kit API SwingVaadinServer.isSwingRendered() method to keep the code runnable on a regular browser and not dependant to a JVaadinPanel. VaadinSwingEvent are fully customizable providing a String unique id (this uniqueness has to be guaranteed by the user) and a HashMap providing the parameters related to this event that could be any object that implements the Serializable interface. On this case the exchanged parameter is the id of the user selected on the Vaadin Grid. 

```java
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
```

To finalize this migration now we need to create the JVaadinPanel that will show the Vaadin view and then prepare the component to handle this event on the Swing side. On this application we are only using one type of event but Swing Kit provides the VaadinSwingEvent::getType() method to let the user to check what event is receiving since all the events created on the Vaadin view are delivered at the level of the JVaadinPanel.

```java 
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
```

![Table migrated](https://user-images.githubusercontent.com/106953874/197409071-2ef487f5-d23a-4df9-aa8a-51e1c044b87c.png)

##### Step 1b: Migrate first the detailed info panel

In this case the user decides to migrate first the bottom panel containing the detailed information form. Then we implement a Vaadin FormLayout that contains all the required TextFields and a Binder for the IPerson data. 

```java
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
```

So far this is just a regular Vaadin view with a form than can already be displayed on a browser. 

![Vaadin Form on the browser](https://user-images.githubusercontent.com/106953874/197409647-b9c8ac28-dfb6-4c95-80b5-d00dfcc2554e.png)

Then to allow the view to be callable from the Swing side when a user of the table is selected we implement on the view the IPersonProvider interface. 

```java
 public class PersonView extends FormLayout implements IPersonProvider {
....
....

    @Override
    public void show(Long id) {
        binder.setBean(PersonsData.getData().get(id));
    }
```

To finalize this migration now we need to create the JVaadinPanel that will call the Vaadin view and then prepare the component to notify the selection item event on the Swing side. 

```java 
        JVaadinPanel personDetails = null;
        try {
            personDetails = SwingVaadinClient.getBuilder().build("http://localhost:8080/person");
            personDetailsProvider = personDetails.as(IPersonProvider.class);
        } catch (SwingVaadinException e) {
            throw new RuntimeException(e);
        }
```
```java 
        tableView.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                Long selectedId = (long) ((ListSelectionModel)e.getSource()).getSelectedIndices()[0] + 1;
                personDetailsProvider.show(selectedId);
            }
        });
```

![Detailed Info Form migrated](https://user-images.githubusercontent.com/106953874/197409849-550eafef-66c5-43b7-a0e8-38ac5d9b8c45.png)


#### Step 2: Combine views

No matter what view we have migrated first now we have to combine both solutions. The only difference with the previous step would be that communication now is from JVaadinPanel to JVaadinPanel. In our demo the event lifecycle is that the selection of an item on the table produces an event that is propagated to the detailed info form. As we have shown on step 1, this means to listen the VaadinSwingEvent on the table view and then call the IPersonProvider interface on the form view. 

```java
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

        ...
        ...

        JVaadinPanel personDetails = null;
        try {
            personDetails = SwingVaadinClient.getBuilder().build("http://localhost:8080/person");
            personDetailsProvider = personDetails.as(IPersonProvider.class);
        } catch (SwingVaadinException e) {
            throw new RuntimeException(e);
        }

```

![Application Migrated Swing UI](https://user-images.githubusercontent.com/106953874/197489201-91e41313-4744-446c-b4d9-f6b38bae102f.png)


#### Step 3: Goodbye Swing

At this point we have migrated all the views of our Swing application. Now we will just need to create a Vaadin View that host both subviews and with the flexibility of Vaadin this a quick easy action. Again the only change to implement is to change the communication between Vaadin views now to a native Vaadin listener as there is no Swing components involved. 

**Main View:**

```java 
public class PersonFullView extends VerticalLayout {
    public PersonFullView() {
        setAlignItems(Alignment.CENTER);
        H2 title = new H2("Person Example Application");
        add(title);
        PersonsTableView table = new PersonsTableView();
        add(table);
        PersonView person = new PersonView();
        add(person);
        table.attachToDetails(person);
    }
}

```

**Table view:**

```java 
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
```    

![Final Vaadin Application](https://user-images.githubusercontent.com/106953874/197497011-6afe668b-ea2e-42e7-9d32-c9f288771e1b.png)

On our demo application this will be the final step and the goodbye to Swing, of course if there were more views to migrate the user could still integrate this Vaadin view containing both table and form into a JVaadinPanel so the complete view is rendered on a Swing application. 
