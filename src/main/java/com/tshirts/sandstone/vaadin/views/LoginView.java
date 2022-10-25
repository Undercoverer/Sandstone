package com.tshirts.sandstone.vaadin.views;

import com.tshirts.sandstone.util.Profile;
import com.tshirts.sandstone.util.managers.LoginManager;
import com.tshirts.sandstone.util.managers.ProfileManager;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

/**
 * The login page of the website. It contains a login form
 * and a link to the registration page.
 * The login form contains a text field for the username,
 * a password field for the password and a button to login.
 * The registration link contains a button to go to the registration page.
 * The login form is validated before the user can log in.
 */
@Route(value = "login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    public LoginView() {
        VerticalLayout loginForm = generateLoginForm();
        this.add(loginForm);
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        setWidth("100%");
        setHeight("100%");
        setMargin(false);
        setPadding(false);
        setSpacing(false);
    }

    /**
     * Generates a login form with a text field for the username,
     * a password field for the password and a button to login.
     * The login form is validated before the user can log in.
     *
     * @return a vertical layout containing the login form
     */
    public VerticalLayout generateLoginForm() {
        VerticalLayout loginForm = new VerticalLayout();
        loginForm.addClassName("login-form");
        loginForm.setWidth("100%");
        loginForm.setHeight("100%");
        loginForm.setMargin(false);
        loginForm.setPadding(true);
        loginForm.setSpacing(false);
        loginForm.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        Paragraph title = new Paragraph("Login");
        title.getStyle().set("font-size", "20px");
        title.getStyle().set("font-weight", "bold");
        title.getStyle().set("color", "#AFAA6A");
        title.getStyle().set("margin-bottom", "15px");

        loginForm.add(title);

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


        TextField username = new TextField();
        username.setLabel("Username");
        username.setPlaceholder("Enter your username");
        username.setRequired(true);
        username.setRequiredIndicatorVisible(true);
        username.setClearButtonVisible(true);
        username.setAutofocus(true);

        PasswordField password = new PasswordField();
        password.setLabel("Password");
        password.setPlaceholder("Enter your password");
        password.setRequired(true);
        password.setRequiredIndicatorVisible(true);
        password.setErrorMessage("Please enter your password");
        password.setClearButtonVisible(true);

        Button login = new Button("Login");
        login.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        login.addClickListener(event -> {
            boolean emptyUsername = username.isEmpty();
            boolean emptyPassword = password.isEmpty();
            if (emptyUsername || emptyPassword) {
                username.setInvalid(emptyUsername);
                password.setInvalid(emptyPassword);
                password.setErrorMessage("Please enter your %s %s %s".formatted(emptyUsername ? "username" : "", emptyUsername && emptyPassword ? "and" : "", emptyPassword ? "password" : ""));
            } else {
                Profile profile = ProfileManager.getInstance().getProfile(username.getValue(), password.getValue());
                if (profile != null) {
                    // Go to main page
                    LoginManager.getInstance().setLoggedIn(true, profile);
                    username.setInvalid(false);
                    password.setInvalid(false);
                    UI.getCurrent().navigate("");
                } else {

                    username.setInvalid(true);
                    password.setInvalid(true);
                    password.setErrorMessage("Invalid username or password");
                }
            }

        });
        form.add(username, password, login);
        form.add(generateRegistrationLink());
        loginForm.add(form);
        return loginForm;
    }

    /**
     * Generates a registration link with a button to go to the registration page.
     *
     * @return a horizontal layout containing the registration link
     */
    public HorizontalLayout generateRegistrationLink() {
        HorizontalLayout registrationLink = new HorizontalLayout();
        registrationLink.addClassName("registration-link");
        registrationLink.getStyle().set("width", "fit-content");
        registrationLink.getStyle().set("height", "fit-content");
        registrationLink.setMargin(false);
        registrationLink.setPadding(true);
        registrationLink.setSpacing(true);
        registrationLink.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        Paragraph text = new Paragraph("Don't have an account?");
        text.getStyle().set("font-size", "15px");
        Button register = new Button("Register");
        register.getStyle().set("font-size", "15px");
        register.addClickListener(event -> register.getUI().ifPresent(ui -> ui.navigate("register")));

        registrationLink.add(text, register);
        return registrationLink;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (LoginManager.getInstance().isLoggedIn()) {
            // Navigate to profile page
            beforeEnterEvent.forwardTo("profile");
        }
    }
}
