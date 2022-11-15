package com.tshirts.sandstone.vaadin.views;

import com.tshirts.sandstone.util.PermissionLevel;
import com.tshirts.sandstone.util.Profile;
import com.tshirts.sandstone.util.managers.LoginManager;
import com.tshirts.sandstone.util.managers.ProductManager;
import com.tshirts.sandstone.vaadin.components.ProductList;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import java.io.File;

@Route(value = "guest")
public class GuestView extends VerticalLayout implements BeforeEnterObserver {
    public GuestView() {
        //added from MainView
        LoginManager loginManager = LoginManager.getInstance();
        this.getStyle().set("overflow", "hidden");
        addClassName("main-view");

        addClassName("guest-view");
        displayAccessMessage();

        //added middle//added from MainView
        add(generateMenuBar());
        //add(generateMainContent());
        add(generateMiddle());
        add(generateFooter());
    }

    private void displayAccessMessage() {
        // TODO display a message saying that the user does not have access to this page

    }

    //Added from MainView but took out other icon buttons besides home and profile
    public static HorizontalLayout generateMenuBar() {
        HorizontalLayout menuBar = new HorizontalLayout();
        menuBar.addClassName("menu-bar");
        menuBar.setWidth("100%");
        menuBar.setHeight("50px");
        menuBar.setMargin(false);
        menuBar.setPadding(false);
        menuBar.setSpacing(false);

        menuBar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        menuBar.setDefaultVerticalComponentAlignment(Alignment.END);
        menuBar.getStyle().set("border-bottom", "1px solid #9E9E9E");

        Paragraph name = new Paragraph("Sandstone");
        name.getStyle().set("font-size", "20px");
        name.getStyle().set("font-weight", "bold");
        name.getStyle().set("color", "#AFAA6A");
        name.getStyle().set("margin-left", "10px");

        menuBar.add(name);

        HorizontalLayout navigationButtons = new HorizontalLayout();
        navigationButtons.addClassName("navigation-buttons");
        navigationButtons.getStyle().set("margin-bottom", "0.75em");
        Button home = new Button(new Icon(VaadinIcon.HOME));
        navigationButtons.add(home);

        //logout button in website's header?
       // Button logout = new Button(new Icon(VaddinIcon.EXIT));
       // navigationButtons.add(logout);

        Button login = new Button(new Icon(VaadinIcon.USER));
        if (LoginManager.getInstance().isLoggedIn()) {
            login.setText("Profile");
            login.addClickListener(e -> {
                // Create dialog to display profile information and a logout button
                Dialog dialog = createProfileDialog();
                dialog.open();
            });
        } else {
            login.setText("Login");
            login.addClickListener(e -> {
                UI.getCurrent().navigate("login");
            });
        }

        navigationButtons.add(login);

        menuBar.add(navigationButtons);
        menuBar.setVerticalComponentAlignment(Alignment.CENTER, navigationButtons);

        return menuBar;
    }

    private static Dialog createProfileDialog() {
        Dialog dialog = new Dialog();
        dialog.getElement().getStyle().set("font-size", "18px");

        dialog.add(new Paragraph("Profile information"));
        Profile loggedInUser = LoginManager.getInstance().getLoggedInUser();
        dialog.add(new Paragraph("Name: %s %s".formatted(loggedInUser.getFirstName(), loggedInUser.getLastName())));
        dialog.add(new Paragraph("Email: %s".formatted(loggedInUser.getEmail())));
        dialog.add(new Paragraph("Phone number: %s".formatted(loggedInUser.getPhone())));
        dialog.add(new Paragraph("Profile Id: %s".formatted(loggedInUser.getProfileId())));
        dialog.add(new Paragraph("Permissions: %s".formatted(loggedInUser.getPermissionLevel())));

        Button logout = new Button("Logout");
        logout.addClickListener(event -> {
            LoginManager.getInstance().setLoggedIn(false, null);
            dialog.close();
            // Wait for the dialog to close before navigating to the login page
            UI.getCurrent().getPage().executeJs("setTimeout(() => { window.location.replace('/login'); }, 100);");
        });

        dialog.add(logout);
        return dialog;
    }

    private HorizontalLayout generateFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addClassName("footer");
        footer.setWidth("100%");
        footer.setAlignItems(Alignment.CENTER);
        footer.setJustifyContentMode(JustifyContentMode.CENTER);
        footer.add(new Text("T-Shirts Sandstone"));
        return footer;
    }

    //generate message in place of where item list should be
    private HorizontalLayout generateMiddle(){
        HorizontalLayout middle = new HorizontalLayout();
        middle.addClassName("middle");
        middle.setWidth("100%");
        middle.setAlignItems(Alignment.CENTER);
        middle.setJustifyContentMode(JustifyContentMode.CENTER);
        /*middle.setJustifyContentMode(JustifyContentMode.BETWEEN);
        middle.setDefaultVerticalComponentAlignment(Alignment.END);
        middle.getStyle().set("border-bottom", "1px solid #9E9E9E");*/
       // middle.add(new Text("Contact manager for privilege elevation!"));
        Paragraph message = new Paragraph("Contact manager for privilege elevation!");
        message.getStyle().set("font-size", "50px");
        message.getStyle().set("font-weight", "bold");
        message.getStyle().set("color", "#AFAA6A");
        message.getStyle().set("margin-left", "10px");
        middle.add(message);

        //middle.add(navigationButtons);
        //middle.setVerticalComponentAlignment(Alignment.CENTER, navigationButtons);



        return middle;
    }

   /* private SplitLayout generateMainContent() {
        SplitLayout mainContent = new SplitLayout();
        mainContent.setOrientation(SplitLayout.Orientation.HORIZONTAL);
        mainContent.setSplitterPosition(100);
        mainContent.getStyle().set("overflow", "hidden");
        //mainContent.addToPrimary(new ProductList());//hide item list from guest

        return mainContent;
    }*/

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (LoginManager.getInstance().isLoggedIn()) {
            if (LoginManager.getInstance().getLoggedInUser().getPermissionLevel() == PermissionLevel.GUEST) {
                return;
            }
            beforeEnterEvent.forwardTo("");
        }
    }
}
