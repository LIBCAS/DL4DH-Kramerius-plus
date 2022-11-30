package cz.inqool.dl4dh.krameriusplus.corev2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(value = "cz.inqool.dl4dh.krameriusplus.corev2")
@SpringBootApplication
public class Initializer {

    @Test
    void loadContext() {}
}
