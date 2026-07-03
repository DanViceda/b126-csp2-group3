package com.petfoodmonitoring.app;

import com.petfoodmonitoring.app.config.DatabaseInitializer;
import com.petfoodmonitoring.app.controller.UserController;

public class Main {

    public static void main(String[] args) {

        DatabaseInitializer.initialize();

        UserController controller = new UserController();

        controller.start();

    }
}
