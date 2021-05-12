package cz.inqool.dl4dh.krameriusplus;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Norbert Bodnar
 */
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "cz.inqool.dl4dh.krameriusplus")
public class DomainApplicationContext {
}
