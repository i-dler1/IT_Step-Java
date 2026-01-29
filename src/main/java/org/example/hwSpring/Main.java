package org.example.hwSpring;

import org.example.hwSpring.config.AppConfig;
import org.example.hwSpring.controller.MainController;
import org.example.hwSpring.controller.MovieController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        MainController controller = context.getBean(MainController.class);
        controller.start();

        // Хоррор фильмы вышедшие после 2000
//        MovieController movieController = context.getBean(MovieController.class);
//        movieController.showHorrorMoviesAfter2000();

    }
}
