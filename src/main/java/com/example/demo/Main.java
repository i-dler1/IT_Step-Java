package com.example.demo;

import com.example.demo.controller.MainController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        MainController controller = context.getBean(MainController.class);

        controller.start();
    }

}
