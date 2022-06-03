package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.object;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DatedObjectFieldGenerator {

    public void generateFields(DatedObject datedObject) {
        if (datedObject.getCreated() == null) {
            datedObject.setCreated(Instant.now());
        }

        datedObject.setUpdated(Instant.now());
    }
}
