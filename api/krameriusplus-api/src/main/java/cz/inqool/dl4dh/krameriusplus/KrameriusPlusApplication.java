package cz.inqool.dl4dh.krameriusplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.MongoDatabaseFactory;

/**
 * @author Norbert Bodnar
 */
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "cz.inqool.dl4dh.krameriusplus")
public class KrameriusPlusApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(KrameriusPlusApplication.class, args);
        MongoDatabaseFactory dbFactory = context.getBean(MongoDatabaseFactory.class);
        dbFactory.getMongoDatabase().drop();
    }
}
