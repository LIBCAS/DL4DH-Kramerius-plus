package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.object;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

@Component
public class DatedObjectListener extends AbstractMongoEventListener<DatedObject> {

    private final DatedObjectFieldGenerator datedObjectFieldGenerator;

    @Autowired
    public DatedObjectListener(DatedObjectFieldGenerator datedObjectFieldGenerator) {
        this.datedObjectFieldGenerator = datedObjectFieldGenerator;
    }

    @Override
    public void onBeforeSave(BeforeSaveEvent<DatedObject> event) {
        datedObjectFieldGenerator.generateFields(event.getSource());
    }
}
