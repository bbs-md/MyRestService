package main.java.loc.service;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");//cconfiguration log4j
        SpringApplication.run(Application.class, args);
    }
}
