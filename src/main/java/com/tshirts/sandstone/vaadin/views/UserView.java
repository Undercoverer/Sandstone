package com.tshirts.sandstone.vaadin.views;

// A User view that is the application made with vaadin

import com.tshirts.sandstone.vaadin.managers.LoginManager;
import com.tshirts.sandstone.vaadin.managers.ProfileManager;
import com.tshirts.sandstone.vaadin.util.PermissionLevel;
import com.tshirts.sandstone.vaadin.util.Profile;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

/**
 * A list of users displayed in a table.
 * Each row in the table represents one user.
 * The table has 4 columns: First Name, Last Name, Email and Role.
 * <p>
 * The only users who can access this page are the admins and the managers, and admins can
 * modify the users' information. Admins can only modify the users' role
 */
@Route("users")
public class UserView extends VerticalLayout implements BeforeEnterObserver {
    Grid<Profile> grid = new Grid<>(Profile.class);
    TextField filterText = new TextField();
    ComboBox<String> filterColumn = new ComboBox<>();


    public UserView() {
        addClassName("html-table");
        setSizeFull();
        configureGrid();

    }

    private void configureGrid() {
        grid.addClassName("users-table-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "username", "email", "phone", "profileId", "permissionLevel");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));


        grid.setItems(ProfileManager.getInstance().getProfiles());

        add(makeToolbar(), grid);
    }

    private HorizontalLayout makeToolbar() {
        filterColumn.setItems("First Name", "Last Name", "Username", "Email", "Phone Number", "Profile ID", "Permission Level");
        filterColumn.setPlaceholder("Filter by");
        filterColumn.setValue("First Name");
        filterColumn.addValueChangeListener(e -> updateList());

        filterText.setPlaceholder("Filter text");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList());

        HorizontalLayout toolbar = new HorizontalLayout(filterColumn, filterText);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        if(filterColumn.getValue() != null && filterText.getValue() != null) {
            System.out.println(filterColumn.getValue());
            System.out.println(filterText.getValue());
            switch (filterColumn.getValue()) {
                case "First Name" -> grid.setItems(ProfileManager.findBy("firstName", filterText.getValue()));
                case "Last Name" -> grid.setItems(ProfileManager.findBy("lastName", filterText.getValue()));
                case "Username" -> grid.setItems(ProfileManager.findBy("username", filterText.getValue()));
                case "Email" -> grid.setItems(ProfileManager.findBy("email", filterText.getValue()));
                case "Phone Number" -> grid.setItems(ProfileManager.findBy("phoneNumber", filterText.getValue()));
                case "Profile ID" -> grid.setItems(ProfileManager.findBy("profileId", filterText.getValue()));
                case "Permission Level" -> grid.setItems(ProfileManager.findBy("permissionLevel", filterText.getValue()));
                default -> {
                }
            }
        } else {
            grid.setItems(ProfileManager.getInstance().getProfiles());
        }
    }
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // Check if logged in
        LoginManager instance = LoginManager.getInstance();
        if (!instance.isLoggedIn()) {
            beforeEnterEvent.forwardTo("/login");
            return;
        }
        // Check if user has permission to access this page
        // If user is not an admin or a manager, redirect to home page
        PermissionLevel permissionLevel = instance.getLoggedInUser().getPermissionLevel();
        if (permissionLevel != null && permissionLevel != PermissionLevel.ADMIN && permissionLevel != PermissionLevel.MANAGER) {
            beforeEnterEvent.forwardTo("/");
        }
    }
}
