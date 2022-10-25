package com.tshirts.sandstone;

import com.tshirts.sandstone.util.managers.LoginManager;
import com.tshirts.sandstone.util.managers.ProfileManager;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Theme(variant = Lumo.DARK)

@SpringBootApplication
public class SandstoneApplication implements AppShellConfigurator {

    static {
        LoginManager.getInstance();
        ProfileManager.getInstance();
    }
    public static void main(String[] args) {
        SpringApplication.run(SandstoneApplication.class, args);
    }

}
