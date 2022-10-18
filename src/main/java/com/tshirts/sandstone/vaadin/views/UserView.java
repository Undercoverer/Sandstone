package com.tshirts.sandstone.vaadin.views;

// A User view that is the application made with vaadin

import com.tshirts.sandstone.vaadin.ProductDetails;
import com.tshirts.sandstone.vaadin.ProductList;
import com.tshirts.sandstone.vaadin.managers.LoginManager;
import com.tshirts.sandstone.vaadin.managers.ProfileManager;
import com.tshirts.sandstone.vaadin.util.PermissionLevel;
import com.tshirts.sandstone.vaadin.util.Profile;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;

/**
 * A list of users displayed in a table.
 * Each row in the table represents one user.
 * The table has 4 columns: First Name, Last Name, Email and Role.
 * <p>
 * The only users who can access this page are the admins and the managers, and admins can
 * modify the users' information. Admins can only modify the users' role
 */
@Route("users")
public class UserView extends VerticalLayout {
    public UserView() {
        addClassName("user-view");
        if (!LoginManager.getInstance().isLoggedIn()) {
            // Navigate to login page
            UI.getCurrent().navigate("login");
            UI.getCurrent().getPage().reload();
        }  else {
            Profile loggedInUser = LoginManager.getInstance().getLoggedInUser();
            System.out.println(loggedInUser.getPermissionLevel());
            if (!(loggedInUser.getPermissionLevel() == PermissionLevel.ADMIN || loggedInUser.getPermissionLevel() == PermissionLevel.MANAGER)) {
                // Navigate to home page
                UI.getCurrent().navigate("");
                UI.getCurrent().getPage().reload();
                // Display error message
                UI.getCurrent().getPage().executeJs("alert('You do not have permission to access the users page.')");
            }
        }
        ArrayList<Profile> users = ProfileManager.getInstance().getProfiles();
        Element table = new Element("table");
        table.setAttribute("id", "users-table");
        Element thead = new Element("thead");
        Element tr = new Element("tr");
        Element th1 = new Element("th");
        th1.setText("First Name");
        Element th2 = new Element("th");
        th2.setText("Last Name");
        Element th3 = new Element("th");
        th3.setText("Email");
        Element th4 = new Element("th");
        th4.setText("Role");
        Element th5 = new Element("th");
        th5.setText("Edit");

        tr.appendChild(th1);
        tr.appendChild(th2);
        tr.appendChild(th3);
        tr.appendChild(th4);
        tr.appendChild(th5);
        thead.appendChild(tr);
        table.appendChild(thead);

        for (Profile user : users) {
            Element row = new Element("tr");
            Element firstName = new Element("td");
            firstName.setText(user.getFirstName());
            Element lastName = new Element("td");
            lastName.setText(user.getLastName());
            Element email = new Element("td");
            email.setText(user.getEmail());
            Element role = new Element("td");
            role.setText(user.getPermissionLevel().toString());
            Element edit = new Element("td");
            Button editButton = new Button("Edit");
            editButton.addClickListener(buttonClickEvent -> {
                // Convert all the text in the table row to input fields
                // so that the user can edit the information
                firstName.removeAllChildren();
                Element firstNameInput = new Element("input");
                firstNameInput.setAttribute("type", "text");
                firstNameInput.setAttribute("value", user.getFirstName());

                firstName.appendChild(firstNameInput);
                lastName.removeAllChildren();
                Element lastNameInput = new Element("input");
                lastNameInput.setAttribute("type", "text");
                lastNameInput.setAttribute("value", user.getLastName());
                lastName.appendChild(lastNameInput);
                email.removeAllChildren();
                Element emailInput = new Element("input");
                emailInput.setAttribute("type", "text");
                emailInput.setAttribute("value", user.getEmail());
                email.appendChild(emailInput);
                role.removeAllChildren();
                Element roleInput = new Element("input");
                roleInput.setAttribute("type", "text");
                roleInput.setAttribute("value", user.getPermissionLevel().toString());
                role.appendChild(roleInput);
                edit.removeAllChildren();
                Button saveButton = new Button("Save");
                saveButton.addClickListener(buttonClickEvent1 -> {
                    // Save the changes to the user's information
                    user.setFirstName(firstNameInput.getAttribute("value"));
                    user.setLastName(lastNameInput.getAttribute("value"));
                    user.setEmail(emailInput.getAttribute("value"));
                    user.setPermissionLevel(PermissionLevel.valueOf(roleInput.getAttribute("value")));

                    // Reload the page
                    UI.getCurrent().getPage().reload();
                });
            });
            row.appendChild(firstName, lastName, email, role);
            table.appendChild(row);
        }

       // Add the element to the DOM
        getElement().appendChild(table);
    }
}
