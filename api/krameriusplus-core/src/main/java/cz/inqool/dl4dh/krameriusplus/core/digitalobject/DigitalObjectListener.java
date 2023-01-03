package cz.inqool.dl4dh.krameriusplus.core.digitalobject;

import cz.inqool.dl4dh.krameriusplus.core.domain.document.DomainDocument;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DigitalObjectListener extends AbstractMongoEventListener<DomainDocument> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<DomainDocument> event) {
        DomainDocument domainDocument = event.getSource();
        if (domainDocument.getCreated() == null) {
            domainDocument.setCreated(Instant.now());
        }

        domainDocument.setUpdated(Instant.now());
    }
}
