package com.tshirts.sandstone.vaadin.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public class ItemView extends VerticalLayout implements BeforeEnterObserver {
    public ItemView() {
        addClassName("item-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(generateItemView());
    }

    private Component generateItemView() {
        // TODO generate the item view
        return null;
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // TODO implement this
    }
}
