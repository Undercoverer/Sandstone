package com.tshirts.sandstone.vaadin.views;

// A Main view that is the application made with vaadin

import com.tshirts.sandstone.vaadin.ProductDetails;
import com.tshirts.sandstone.vaadin.ProductList;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;

@Route("")
//@CssImport("./styles/shared-styles.css")
public class MainView extends VerticalLayout {

    /**
     * The main page of the website. It contains a menu bar on top, a resizable horizontal layout in the middle and a footer at the bottom.
     * The menu bar contains the logo, the name of the website and the navigation buttons.
     * The horizontal layout contains the main content of the website.
     * The footer contains the name of the website and the contact information.
     */
    public MainView() {
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

        Button about = new Button(new Icon(VaadinIcon.INFO_CIRCLE));
        navigationButtons.add(about);

        Button cart = new Button(new Icon(VaadinIcon.CART));
        navigationButtons.add(cart);

        Button login = new Button(new Icon(VaadinIcon.USER));
        login.addClickListener(e -> {
            login.getUI().ifPresent(ui -> ui.navigate("login"));
        });
        navigationButtons.add(login);

        menuBar.add(navigationButtons);
        menuBar.setVerticalComponentAlignment(Alignment.CENTER, navigationButtons);

        return menuBar;
    }

    public static String placeholderImageURL(int width, int height, String backColor, String textColor, String text) {
        return "https://via.placeholder.com/" + ((width == height) ? width : width + "x" + height) + "/" + backColor + "/" + textColor + "?text=" + text;
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
        mainContent.addToPrimary(new ProductList("src/main/resources/products.json"));
        mainContent.addToSecondary(new ProductDetails());
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
}
