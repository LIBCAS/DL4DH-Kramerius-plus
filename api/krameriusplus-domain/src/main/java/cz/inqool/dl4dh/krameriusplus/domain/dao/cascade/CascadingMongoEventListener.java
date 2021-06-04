package cz.inqool.dl4dh.krameriusplus.domain.dao.cascade;

import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * @author Norbert Bodnar
 */
@Component
public class CascadingMongoEventListener extends AbstractMongoEventListener<Object> {

    private final PublicationRepository publicationRepository;

    @Autowired
    public CascadingMongoEventListener(PublicationRepository publicationRepository) {
        this.publicationRepository = publicationRepository;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Object source = event.getSource();
        ReflectionUtils.doWithFields(source.getClass(),
                new CascadeCallback(source, publicationRepository));
    }
}
