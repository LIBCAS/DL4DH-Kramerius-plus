package cz.inqool.dl4dh.krameriusplus.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author Norbert Bodnar
 */
@ComponentScan(basePackages = "cz.inqool.dl4dh.krameriusplus")
@EntityScan(basePackages = "cz.inqool.dl4dh.krameriusplus")
@SpringBootApplication
//@EnableMongoRepositories(basePackages = "cz.inqool.dl4dh.krameriusplus")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
