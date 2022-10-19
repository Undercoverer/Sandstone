package com.tshirts.sandstone.vaadin.views;

import com.tshirts.sandstone.vaadin.managers.LoginManager;
import com.tshirts.sandstone.vaadin.managers.ProfileManager;
import com.tshirts.sandstone.vaadin.util.PermissionLevel;
import com.tshirts.sandstone.vaadin.util.Profile;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import java.util.Objects;

@Route(value = "register")
public class RegisterView extends VerticalLayout implements BeforeEnterObserver {
    public RegisterView() {
        VerticalLayout registrationForm = generateRegistrationForm();
        this.add(registrationForm);
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        setWidth("100%");
        setHeight("100%");
        setMargin(false);
        setPadding(false);
        setSpacing(false);
    }

    public VerticalLayout generateRegistrationForm() {
        VerticalLayout registrationForm = new VerticalLayout();
        registrationForm.addClassName("registration-form");
        registrationForm.setWidth("150%");
        registrationForm.setHeight("100%");
        registrationForm.setMargin(false);
        registrationForm.setPadding(true);
        registrationForm.setSpacing(false);
        registrationForm.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        Paragraph title = new Paragraph("Register");
        title.getStyle().set("font-size", "20px");
        title.getStyle().set("font-weight", "bold");
        title.getStyle().set("color", "#AFAA6A");
        title.getStyle().set("margin-bottom", "15px");

        registrationForm.add(title);

        VerticalLayout form = new VerticalLayout();
        form.addClassName("form");
        form.getStyle().set("width", "fit-content");
        form.getStyle().set("height", "fit-content");
        form.setMargin(false);
        form.setPadding(true);
        form.setSpacing(true);
        form.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        form.getStyle().set("border", "1px solid #0F1F3F"); // Darker blue border
        form.getStyle().set("border-radius", "5px");
        form.getStyle().set("box-shadow", "0 0 10px #0F0F0F");

        TextField fullName = new TextField();
        fullName.setLabel("Full Name");
        fullName.setPlaceholder("John Doe");
        fullName.setRequired(true);
        fullName.setRequiredIndicatorVisible(true);
        fullName.setClearButtonVisible(true);
        fullName.setAutofocus(true);

        TextField email = new TextField();
        email.setLabel("Email");
        email.setPlaceholder("Enter an email");
        email.setRequired(true);
        email.setRequiredIndicatorVisible(true);
        email.setClearButtonVisible(true);

        TextField username = new TextField();
        username.setLabel("Username");
        username.setPlaceholder("Enter a username");
        username.setRequired(true);
        username.setRequiredIndicatorVisible(true);
        username.setClearButtonVisible(true);

        PasswordField password = new PasswordField();
        password.setLabel("Password");
        password.setPlaceholder("Enter a password");
        password.setRequired(true);
        password.setRequiredIndicatorVisible(true);
        password.setErrorMessage("Please enter a password");
        password.setClearButtonVisible(true);

        TextField phoneNumber = new TextField();
        phoneNumber.setLabel("Phone Number");
        phoneNumber.setPlaceholder("Enter a phone number");
        phoneNumber.setRequired(true);
        phoneNumber.setRequiredIndicatorVisible(true);
        phoneNumber.setClearButtonVisible(true);
        // Add custom validation
        String countryCodeRegex = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";
        phoneNumber.setPattern(countryCodeRegex);


        Button register = new Button("Register");
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        register.addClickListener(event -> {
            boolean emptyFields = fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || phoneNumber.isEmpty();


            if (emptyFields) {
                fullName.setInvalid(true);
                email.setInvalid(true);
                username.setInvalid(true);
                password.setInvalid(true);
                phoneNumber.setInvalid(true);
                phoneNumber.setErrorMessage("Please enter all fields");
            } else {
                if (ProfileManager.getInstance().getProfile(email.getValue(), password.getValue()) == null) {
                    // Create new account
                    String[] name = fullName.getValue().split(" ");
                    Profile profile = new Profile(username.getValue(), name[0], name[1], email.getValue(), phoneNumber.getValue(), password.getValue(), Objects.hashCode(username), PermissionLevel.USER);
                    ProfileManager.getInstance().addProfile(profile);
                    LoginManager.getInstance().setLoggedIn(true, profile);
                    // Navigate to main page
                    email.setInvalid(false);
                    password.setInvalid(false);
                    UI.getCurrent().navigate("");
                    UI.getCurrent().getPage().reload();
                }
            }

        });
        form.add(fullName, email, username, password, phoneNumber, register);
        registrationForm.add(form);
        return registrationForm;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (LoginManager.getInstance().isLoggedIn()) {
            beforeEnterEvent.forwardTo("");
        }
    }
}
