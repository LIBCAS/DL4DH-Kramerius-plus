package cz.inqool.dl4dh.persistence;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Norbert Bodnar
 */
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "cz.inqool.dl4dh.persistence")
public class PersistenceApplicationContext {
}
