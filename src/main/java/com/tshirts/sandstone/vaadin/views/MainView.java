package com.tshirts.sandstone.vaadin.views;

// A Main view that is the application made with vaadin

import com.google.gson.stream.JsonReader;
import com.tshirts.sandstone.util.PermissionLevel;
import com.tshirts.sandstone.util.Profile;
import com.tshirts.sandstone.util.managers.LoginManager;
import com.tshirts.sandstone.util.managers.ProductManager;
import com.tshirts.sandstone.vaadin.components.ProductList;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Route("")
//@CssImport("./styles/shared-styles.css")
public class MainView extends VerticalLayout implements BeforeEnterObserver {

    /**
     * The main page of the website. It contains a menu bar on top, a resizable horizontal layout in the middle and a footer at the bottom.
     * The menu bar contains the logo, the name of the website and the navigation buttons.
     * The horizontal layout contains the main content of the website.
     * The footer contains the name of the website and the contact information.
     */
    public MainView() {
        LoginManager loginManager = LoginManager.getInstance();
        this.getStyle().set("overflow", "hidden");
        addClassName("main-view");
        add(generateMenuBar());
        add(generateMainContent());
        add(generateFooter());
    }

    /**
     * Generates a menu bar with the logo, the name of the website and the navigation buttons.
     *
     * @return a horizontal layout containing the menu bar
     */
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

        Button Users = new Button(new Icon(VaadinIcon.USERS));
        Users.setText("Users");
        Users.addClickListener(e -> {
            UI.getCurrent().navigate("users");
        });
        navigationButtons.add(Users);

        Button cart = new Button(new Icon(VaadinIcon.FILE_ADD));
        cart.setText("Import File");
        cart.addClickListener(MainView::importFileEventCallback);
        navigationButtons.add(cart);

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
        dialog.add(new Paragraph("Username: %s".formatted(loggedInUser.getUsername())));
        dialog.add(new Paragraph("Email: %s".formatted(loggedInUser.getEmail())));
        dialog.add(new Paragraph("Phone number: %s".formatted(loggedInUser.getPhone())));
        dialog.add(new Paragraph("Profile Id: %s".formatted(loggedInUser.getProfileId())));
        dialog.add(new Paragraph("Permissions: %s".formatted(loggedInUser.getPermissionLevel())));

        Button logout = new Button("Logout");
        logout.addClickListener(event -> {
            LoginManager.getInstance().setLoggedIn(false, null);
            dialog.close();
            UI.getCurrent().navigate("/login");
        });

        dialog.add(logout);
        return dialog;
    }

    public static String placeholderImageURL(int width, int height, String backColor, String textColor, String
            text) {
        return "https://via.placeholder.com/" + ((width == height) ? width : width + "x" + height) + "/" + backColor + "/" + textColor + "?text=" + text;
    }

    private static void importFileEventCallback(ClickEvent<Button> e) {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");
        dialog.setHeight("200px");
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        H4 title = new H4("Import File");
        dialog.add(title);

        Paragraph hint = new Paragraph("File size must be less than 10MB. Only .json files are accepted.");
        dialog.add(hint);

        Upload upload = new Upload();
        upload.setAcceptedFileTypes("application/json");
        upload.setDropAllowed(true);
        upload.setMaxFileSize(10_000_000);
        upload.setReceiver(MainView::receiveUpload);

        upload.addSucceededListener(MainView::fileUploadSuccessCallback);
        dialog.add(upload);
        dialog.open();
    }

    private static void fileUploadSuccessCallback(SucceededEvent succeededEvent) {
        // Create confirmation dialog to display general information about the file
        // and ask the user to confirm the import
        Dialog dialog = new Dialog();
        H4 title = new H4("File Details");
        dialog.add(title);

        // Get the file name
        String fileName = succeededEvent.getFileName();
        dialog.add(new Paragraph("File name: %s".formatted(fileName)));

        // Get the file size
        long fileSize = succeededEvent.getContentLength();
        dialog.add(new Paragraph("File size: %s".formatted(fileSize)));

        // Check to see what data is in the file (profiles or products)
        String fileType = "Unknown";
//        JsonReader jsonReader = new JsonReader(new
    }

    private static OutputStream receiveUpload(String fileName, String mimeType) {
        File file = new File("src/main/resources/uploads/" + fileName);
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Generates the main content panel.
     * A horizontally resizable panel that contains the product list on the left and the product details on the right.
     *
     * @return the main content panel.
     */
    private SplitLayout generateMainContent() {
        SplitLayout mainContent = new SplitLayout();
        mainContent.setOrientation(SplitLayout.Orientation.HORIZONTAL);
        mainContent.setSplitterPosition(100);
        mainContent.getStyle().set("overflow", "hidden");
        mainContent.addToPrimary(new ProductList());

        return mainContent;
    }

    /**
     * Generates the footer panel.
     * A panel that contains the name of the website and the contact information.
     *
     * @return the footer panel.
     */
    private HorizontalLayout generateFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addClassName("footer");
        footer.setWidth("100%");
        footer.setAlignItems(Alignment.CENTER);
        footer.setJustifyContentMode(JustifyContentMode.CENTER);
        footer.add(new Text("T-Shirts Sandstone"));
        return footer;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!LoginManager.getInstance().isLoggedIn()) {
            beforeEnterEvent.forwardTo("login");
        } else if (LoginManager.getInstance().getLoggedInUser().getPermissionLevel() == PermissionLevel.GUEST) {
            beforeEnterEvent.forwardTo("guest");
        }
    }
}
