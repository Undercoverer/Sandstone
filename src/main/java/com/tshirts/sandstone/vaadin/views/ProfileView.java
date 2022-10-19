package com.tshirts.sandstone.vaadin.views;

import com.tshirts.sandstone.vaadin.managers.LoginManager;
import com.tshirts.sandstone.vaadin.util.Profile;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route(value = "profile")
public class ProfileView extends VerticalLayout implements BeforeEnterObserver {
    public ProfileView() {
        addClassName("profile-view");
        // All profile information goes here
        LoginManager loginManager = LoginManager.getInstance();

        Profile loggedInUser = loginManager.getLoggedInUser();
        add(generateFromProfile(loggedInUser));

    }


    public VerticalLayout generateFromProfile(Profile profile) {
        // Font for all text
        String font = "font-family: 'Roboto', sans-serif;";
        // Font for all headings
        String headingFont = "font-family: 'Roboto', sans-serif; font-weight: bold;";

        VerticalLayout profileLayout = new VerticalLayout();
        profileLayout.addClassName("profile-layout");
        profileLayout.setMargin(false);
        profileLayout.setPadding(true);
        profileLayout.setSpacing(false);
        profileLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);


        Paragraph profileName = new Paragraph(profile.getFirstName() + " " + profile.getLastName());
        profileName.addClassName("profile-name");
        profileName.getElement().setAttribute("style", headingFont);


        Paragraph profileEmail = new Paragraph("Email: " + profile.getEmail());
        profileEmail.addClassName("profile-email");
        profileEmail.getElement().setAttribute("style", font);

        Paragraph profilePhone = new Paragraph("Phone Number:  " + profile.getPhone());
        profilePhone.addClassName("profile-phone");
        profilePhone.getElement().setAttribute("style", font);

        Paragraph profileID = new Paragraph("Profile ID: " + profile.getProfileId());
        profileID.addClassName("profile-id");
        profileID.getElement().setAttribute("style", font);

        Paragraph profileRole = new Paragraph("Role: " + profile.getPermissionLevel().toString());
        profileRole.addClassName("profile-role");
        profileRole.getElement().setAttribute("style", font);

        profileLayout.add(profileName);
        profileLayout.add(profileEmail);
        profileLayout.add(profilePhone);
        profileLayout.add(profileID);
        profileLayout.add(profileRole);

        return profileLayout;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // Check if user is logged in
        if (!LoginManager.getInstance().isLoggedIn()) {
            beforeEnterEvent.forwardTo("login");
        }
    }
}
