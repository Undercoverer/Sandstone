package com.tshirts.sandstone.vaadin.views;

import com.tshirts.sandstone.util.PermissionLevel;
import com.tshirts.sandstone.util.managers.LoginManager;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route(value = "guest")
public class GuestView extends VerticalLayout implements BeforeEnterObserver {
    public GuestView() {
        addClassName("guest-view");
        displayAccessMessage();
    }

    private void displayAccessMessage() {
        // TODO display a message saying that the user does not have access to this page
    }

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
