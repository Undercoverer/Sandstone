package com.tshirts.sandstone.vaadin.components;

import com.tshirts.sandstone.util.PermissionLevel;
import com.tshirts.sandstone.util.Product;
import com.tshirts.sandstone.util.Product;
import com.tshirts.sandstone.util.Util;
import com.tshirts.sandstone.util.managers.LoginManager;
import com.tshirts.sandstone.util.managers.ProductManager;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Image;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

//TODO Fix static shit
public class ProductList extends VerticalLayout implements BeforeEnterObserver {
    Grid<Product> grid = new Grid<>(Product.class, false);
    List<Product> products;
    TextField filterText = new TextField();
    ComboBox<String> filterColumn = new ComboBox<>();


    public ProductList() {
        products = new ArrayList<>(Util.DB.getAll(Product.class));
        addClassName("html-table");
        setSizeFull();
        configureGrid();

    }

    public void update() {
        this.products = Util.DB.getAll(Product.class).stream().toList();
        grid.setItems(products);
    }

    private void configureGrid() {
        grid.setColumns();
        grid.addClassName("products-table-grid");
        grid.addComponentColumn(item -> {
            Image image = new Image(item.getImage(), "No image");
            image.setHeight("100px");
            image.setWidth("100px");
            return image;
        }).setHeader("Image");

        grid.addColumn(Product::getName).setHeader("Name");
        grid.addColumn(Product::getDescription).setHeader("Description");
        grid.addColumn(Product::getPrice).setHeader("Price");
        grid.setItems(ProductManager.getInstance().getAll());
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        add(makeToolbar());
        add(grid);
        System.out.println(grid.getColumns());
    }

    private HorizontalLayout makeToolbar() {
        filterColumn.setItems("Name", "Description", "Price", "Price Less Than", "Price Greater Than");

        filterColumn.setPlaceholder("Filter by");
        filterColumn.setValue("Name");
        filterColumn.addValueChangeListener(e -> updateList());

        filterText.setPlaceholder("Filter text");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList());

        HorizontalLayout toolbarLeft = new HorizontalLayout(filterColumn, filterText);

        // Add home button
        HorizontalLayout toolbar = new HorizontalLayout(toolbarLeft);
        toolbar.setAlignSelf(Alignment.STRETCH, toolbar);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        if (filterColumn.getValue() != null && filterText.getValue() != null) {
            switch (filterColumn.getValue()) {
                case "Name" -> grid.setItems(ProductManager.getInstance().findBy("name", filterText.getValue()));
                case "Description" ->
                        grid.setItems(ProductManager.getInstance().findBy("description", filterText.getValue()));
                case "Price" ->
                        grid.setItems(ProductManager.getInstance().findBy("priceEqualTo", filterText.getValue()));
                case "Price Less Than" ->
                        grid.setItems(ProductManager.getInstance().findBy("priceLessThan", filterText.getValue()));
                case "Price Greater Than" ->
                        grid.setItems(ProductManager.getInstance().findBy("priceGreaterThan", filterText.getValue()));

            }
        } else {
            grid.setItems(ProductManager.getInstance().getAll());
        }
        grid.recalculateColumnWidths();

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

