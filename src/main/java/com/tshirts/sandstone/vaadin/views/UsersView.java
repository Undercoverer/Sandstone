package com.tshirts.sandstone.vaadin.views;

// A User view that is the application made with vaadin


import com.tshirts.sandstone.util.PermissionLevel;
import com.tshirts.sandstone.util.Profile;
import com.tshirts.sandstone.util.managers.LoginManager;
import com.tshirts.sandstone.util.managers.ProfileManager;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

/**
 * A list of users displayed in a table.
 * Each row in the table represents one user.
 * The table has 7 columns: First Name, Last Name, Username, Email, Phone, ProfileID and Role.
 * <p>
 * The only users who can access this page are the admins and the managers, and admins can
 * modify the users' information. Admins can only modify the users' role
 */
@Route("users")
public class UsersView extends VerticalLayout implements BeforeEnterObserver {
    Grid<Profile> grid = new Grid<>(Profile.class);
    TextField filterText = new TextField();
    ComboBox<String> filterColumn = new ComboBox<>();


    public UsersView() {
        addClassName("html-table");
        setSizeFull();
        configureGrid();

    }

    private void configureGrid() {
        grid.addClassName("users-table-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "username", "email", "phone", "profileId", "permissionLevel");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));


        grid.setItems(ProfileManager.getInstance().getAll());

        GridContextMenu<Profile> profileGridContextMenu = grid.addContextMenu();
        profileGridContextMenu.addItem("Delete", event -> {
            if (event.getItem().isPresent()) {
                // Create a popup window to confirm the deletion
                Dialog dialog = new Dialog();
                dialog.setCloseOnEsc(true);
                dialog.setCloseOnOutsideClick(true);
                dialog.setModal(true);

                VerticalLayout form = new VerticalLayout();
                form.setAlignItems(Alignment.CENTER);
                form.setJustifyContentMode(JustifyContentMode.CENTER);
                form.setPadding(true);
                dialog.add(form);


                Label label = new Label("Are you sure you want to delete this user?");
                label.getStyle().set("font-size", "2em");
                label.getStyle().set("font-weight", "bold");

                TextField confirmation = new TextField();
                confirmation.setLabel("Type 'DELETE' to confirm");
                confirmation.setRequired(true);
                confirmation.setRequiredIndicatorVisible(true);
                confirmation.setErrorMessage("Confirmation is required");

                HorizontalLayout buttons = new HorizontalLayout();
                buttons.setAlignItems(Alignment.CENTER);
                buttons.setJustifyContentMode(JustifyContentMode.CENTER);

                Button delete = new Button("Delete");
                delete.addClickListener(event1 -> {
                    // Validate the form
                    if (confirmation.isEmpty() || !confirmation.getValue().equals("DELETE")) {
                        return;
                    }
                    // Delete the user
                    ProfileManager.getInstance().remove(event.getItem().get());
                    // Refresh the table
                    grid.setItems(ProfileManager.getInstance().getAll());
                    updateList();
                    dialog.close();
                });

                Button cancel = new Button("Cancel");
                cancel.addClickListener(event1 -> {
                    dialog.close();
                });

                buttons.add(delete, cancel);
                form.add(label, confirmation, buttons);
                dialog.open();
            }
        });
        profileGridContextMenu.addItem("Modify", event -> {
            if (event.getItem().isPresent()) {
                Profile profile = event.getItem().get();
                // Create a popup window for editing the user's information only if the user has lower permissions
                Profile loggedInUser = LoginManager.getInstance().getLoggedInUser();
                if (loggedInUser.getPermissionLevel().ordinal() <= profile.getPermissionLevel().ordinal()) {
                    return;
                }
                Dialog dialog = new Dialog();
                dialog.setCloseOnEsc(true);
                dialog.setCloseOnOutsideClick(true);
                dialog.setModal(true);

                // Create a form for editing the user's information
                VerticalLayout form = new VerticalLayout();
                form.setAlignItems(Alignment.CENTER);
                form.setJustifyContentMode(JustifyContentMode.CENTER);

                // Create a text field for editing the user's first name
                TextField firstName = new TextField();
                firstName.setLabel("First Name");
                firstName.setValue(profile.getFirstName());
                firstName.setRequired(true);
                firstName.setRequiredIndicatorVisible(true);
                firstName.setErrorMessage("First name is required");

                // Create a text field for editing the user's last name
                TextField lastName = new TextField();
                lastName.setLabel("Last Name");
                lastName.setValue(profile.getLastName());
                lastName.setRequired(true);
                lastName.setRequiredIndicatorVisible(true);
                lastName.setErrorMessage("Last name is required");

                // Create a text field for editing the user's email
                TextField email = new TextField();
                email.setLabel("Email");
                email.setValue(profile.getEmail());
                email.setRequired(true);
                email.setRequiredIndicatorVisible(true);
                email.setErrorMessage("Email is required");

                // Create a text field for editing the user's phone number
                TextField phone = new TextField();
                phone.setLabel("Phone");
                phone.setValue(profile.getPhone());
                phone.setRequired(true);
                phone.setRequiredIndicatorVisible(true);
                phone.setErrorMessage("Phone number is required");

                // Create a combo box for editing the user's role
                ComboBox<String> permissionLevel = new ComboBox<>();
                permissionLevel.setLabel("Role");
                // Allow changing the role up to the current user's role
                if (loggedInUser.getPermissionLevel().equals(PermissionLevel.ADMIN)) {
                    permissionLevel.setItems(PermissionLevel.MANAGER.toString(), PermissionLevel.USER.toString(), PermissionLevel.GUEST.toString());
                } else if (loggedInUser.getPermissionLevel().equals(PermissionLevel.MANAGER)) {
                    permissionLevel.setItems(PermissionLevel.USER.toString(), PermissionLevel.GUEST.toString());
                }
                permissionLevel.setValue(profile.getPermissionLevel().toString());
                permissionLevel.setRequired(true);

                // Create a horizontal layout for the buttons
                HorizontalLayout buttons = new HorizontalLayout();
                buttons.setAlignItems(Alignment.CENTER);
                buttons.setJustifyContentMode(JustifyContentMode.CENTER);

                // Create a button for saving the changes
                Button save = new Button("Save");
                save.addClickListener(event1 -> {
                    // Validate the form
                    if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || permissionLevel.isEmpty()) {
                        return;
                    }
                    // Update the user's information
                    profile.setFirstName(firstName.getValue());
                    profile.setLastName(lastName.getValue());
                    profile.setEmail(email.getValue());
                    profile.setPhone(phone.getValue());
                    profile.setPermissionLevel(PermissionLevel.valueOf(permissionLevel.getValue()));
//                    ProfileManager.getInstance().removeProfile(profile);
//                    ProfileManager.getInstance().addProfile(profile);
                    // Refresh the table
                    grid.setItems(ProfileManager.getInstance().getAll());
                    dialog.close();
                });

                // Create a button for cancelling the changes
                Button cancel = new Button("Cancel");
                cancel.addClickListener(event1 -> {
                    dialog.close();
                });

                // Add the form fields to the form
                form.add(firstName, lastName, email, phone, permissionLevel, buttons);
                // Add the buttons to the horizontal layout
                buttons.add(save, cancel);

                // Add the form to the popup window
                dialog.add(form);
                // Open the popup window
                dialog.open();
            }
        });
        profileGridContextMenu.addItem("Reset Password", event -> {
            if (event.getItem().isPresent()){
                Profile profile = event.getItem().get();
                // Create a popup window for editing the user's information only if the user has lower permissions
                Profile loggedInUser = LoginManager.getInstance().getLoggedInUser();
                if (loggedInUser.getPermissionLevel().ordinal() <= profile.getPermissionLevel().ordinal()) {
                    return;
                }
                Dialog dialog = new Dialog();
                dialog.setCloseOnEsc(true);
                dialog.setCloseOnOutsideClick(true);
                dialog.setModal(true);

                // Create a form for editing the user's information
                VerticalLayout form = new VerticalLayout();
                form.setAlignItems(Alignment.CENTER);
                form.setJustifyContentMode(JustifyContentMode.CENTER);
                form.setPadding(true);

                // Create a text field for editing the user's password
                PasswordField password = new PasswordField();
                password.setLabel("Password");
                password.setRequired(true);
                password.setRequiredIndicatorVisible(true);
                password.setErrorMessage("Password is required");

                // Create a text field for editing the user's password confirmation
                PasswordField passwordConfirmation = new PasswordField();
                passwordConfirmation.setLabel("Confirm Password");
                passwordConfirmation.setRequired(true);
                passwordConfirmation.setRequiredIndicatorVisible(true);
                passwordConfirmation.setErrorMessage("Password confirmation is required");

                // Create a horizontal layout for the buttons
                HorizontalLayout buttons = new HorizontalLayout();
                buttons.setAlignItems(Alignment.CENTER);
                buttons.setJustifyContentMode(JustifyContentMode.CENTER);

                // Create a button for saving the changes
                Button save = new Button("Save");
                save.addClickListener(event1 -> {
                    // Validate the form
                    if (password.isEmpty() || passwordConfirmation.isEmpty() || !password.getValue().equals(passwordConfirmation.getValue())) {
                        return;
                    }
                    // Update the user's information
                    profile.setPassword(password.getValue());
                    // Refresh the table
                    grid.setItems(ProfileManager.getInstance().getAll());
                    dialog.close();
                });

                // Create a button for cancelling the changes
                Button cancel = new Button("Cancel");
                cancel.addClickListener(event1 -> {
                    dialog.close();
                });

                // Add the form fields to the form
                form.add(password, passwordConfirmation, buttons);
                // Add the buttons to the horizontal layout
                buttons.add(save, cancel);

                // Add the form to the popup window
                dialog.add(form);
                // Open the popup window
                dialog.open();
            }
        });
        profileGridContextMenu.setOpenOnClick(true);

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

        HorizontalLayout toolbarLeft = new HorizontalLayout(filterColumn, filterText);

        // Add home button
        Button homeButton = new Button("Home");
        homeButton.addClickListener(event -> {
            UI.getCurrent().navigate("");
        });
        homeButton.setIcon(new Icon(VaadinIcon.HOME));
        HorizontalLayout toolbar = new HorizontalLayout(toolbarLeft, homeButton);
        toolbar.setAlignSelf(Alignment.STRETCH, toolbar);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        if (filterColumn.getValue() != null && filterText.getValue() != null) {
            switch (filterColumn.getValue()) {
                case "First Name" -> grid.setItems(ProfileManager.getInstance().findBy("firstName", filterText.getValue()));
                case "Last Name" -> grid.setItems(ProfileManager.getInstance().findBy("lastName", filterText.getValue()));
                case "Username" -> grid.setItems(ProfileManager.getInstance().findBy("username", filterText.getValue()));
                case "Email" -> grid.setItems(ProfileManager.getInstance().findBy("email", filterText.getValue()));
                case "Phone Number" -> grid.setItems(ProfileManager.getInstance().findBy("phoneNumber", filterText.getValue()));
                case "Profile ID" -> grid.setItems(ProfileManager.getInstance().findBy("profileId", filterText.getValue()));
                case "Permission Level" ->
                        grid.setItems(ProfileManager.getInstance().findBy("permissionLevel", filterText.getValue()));
                default -> {
                }
            }
        } else {
            grid.setItems(ProfileManager.getInstance().getAll());
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
