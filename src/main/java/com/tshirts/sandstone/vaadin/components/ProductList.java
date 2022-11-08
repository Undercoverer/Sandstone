package com.tshirts.sandstone.vaadin.components;

import com.tshirts.sandstone.util.PermissionLevel;
import com.tshirts.sandstone.util.Product;
import com.tshirts.sandstone.util.Profile;
import com.tshirts.sandstone.util.Util;
import com.tshirts.sandstone.util.managers.LoginManager;
import com.tshirts.sandstone.util.managers.ProductManager;
import com.tshirts.sandstone.util.managers.ProfileManager;
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
import java.util.List;

public class ProductList extends VerticalLayout {
    Grid<Product> grid = new Grid<>(Product.class);

    public ProductList() {
        addClassName("html-table");
        setSizeFull();
        configureGrid();

    }

    private void configureGrid() {
        grid.addClassName("products-table-grid");
        grid.setSizeFull();
        grid.setColumns("name");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.setItems(ProductManager.getInstance().getAll());
    }

    private void updateList() {
        grid.setItems(ProductManager.getInstance().getAll());
    }
}

