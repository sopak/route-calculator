package cz.jcode.routecalculator.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Application entry point
 */
@SpringBootApplication
@ComponentScan("cz.jcode.routecalculator.*")
public class RouteCalculatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(RouteCalculatorApplication.class, args);
    }
}

