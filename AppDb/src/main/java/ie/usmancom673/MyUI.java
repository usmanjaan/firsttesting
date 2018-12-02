package ie.usmancom673;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.*;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Connection connection = null;
        String connectionString = "jdbc:sqlserver://classdbtest.database.windows.net:1433;" + "database=classDB;"
                + "user=usmanshahid18@classdbtest;" + "password=Appleipod@1;" + "encrypt=true;"
                + "trustServerCertificate=false;" + "hostNameInCertificate=*.database.windows.net;"
                + "loginTimeout=30;";

        // Create the connection object

        final VerticalLayout layout = new VerticalLayout();
        final TextField name = new TextField();
        name.setCaption("Type your name here:");

        Button button = new Button("Please work 9");
        button.addClickListener(e -> {
            layout.addComponent(new Label("Thanks " + name.getValue() + ", it works!"));
        });

        try {
            // Connect with JDBC driver to a database
            connection = DriverManager.getConnection(connectionString);
            // Add a label to the web app with the message and name of the database we
            // connected to
            // layout.addComponent(new Label("Connected to database: " +
            // connection.getCatalog()));
            // ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM
            // customerTable;");
            // Convert the resultset that comes back into a List - we need a Java class to
            // represent the data (Customer.java in this case)
            // Convert the resultset that comes back into a List - we need a Java class to
            // represent the data (Customer.java in this case)
            List<Customer> customers = new ArrayList<Customer>();
            // While there are more records in the resultset
            ResultSet rs = connection.createStatement()
                    .executeQuery("SELECT * FROM customerTable WHERE paid = 'false' ORDER BY AMOUNT DESC;");
            while (rs.next()) {
                // Add a new Customer instantiated with the fields from the record (that we
                // want, we might not want all the fields, note how I skip the id)
                customers.add(new Customer(rs.getString("first_name"), rs.getString("last_name"), rs.getBoolean("paid"),
                        rs.getDouble("amount")));
            }
            // Add my component, grid is templated with Customer
            Grid<Customer> myGrid = new Grid<>();
            // Set the items (List)
            myGrid.setItems(customers);
            // Configure the order and the caption of the grid
            myGrid.addColumn(Customer::getFirst_name).setCaption("Name");
            myGrid.addColumn(Customer::getLast_name).setCaption("Surname");
            myGrid.addColumn(Customer::getAmount).setCaption("Total Amount");
            myGrid.addColumn(Customer::isPaid).setCaption("Paid");
            // Add the grid to the list
            layout.addComponent(myGrid);
        } catch (Exception e) {
            // This will show an error message if something went wrong
            layout.addComponent(new Label(e.getMessage()));
        }
        layout.addComponents(name, button);

        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
