package cz.inqool.dl4dh.krameriusplus.core;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(value = "cz.inqool.dl4dh.krameriusplus")
@EntityScan(value = "cz.inqool.dl4dh.krameriusplus")
@SpringBootApplication
public class TestCoreContext {
}
