package org.example.hwSpring.config;

import org.springframework.context.annotation.*;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("org.example.hwSpring")
public class AppConfig {

}