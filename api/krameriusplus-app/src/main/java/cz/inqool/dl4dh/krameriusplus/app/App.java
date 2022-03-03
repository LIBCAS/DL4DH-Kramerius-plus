package cz.inqool.dl4dh.krameriusplus.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Norbert Bodnar
 */
@ComponentScan(basePackages = "cz.inqool.dl4dh.krameriusplus")
@EntityScan(basePackages = "cz.inqool.dl4dh.krameriusplus")
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
