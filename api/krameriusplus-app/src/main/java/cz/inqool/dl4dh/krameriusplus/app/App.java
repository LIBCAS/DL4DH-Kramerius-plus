package cz.inqool.dl4dh.krameriusplus.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Norbert Bodnar
 */
@ComponentScan(basePackages = "cz.inqool.dl4dh.krameriusplus")
@EntityScan(basePackages = "cz.inqool.dl4dh.krameriusplus")
@SpringBootConfiguration
@EnableAutoConfiguration
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
