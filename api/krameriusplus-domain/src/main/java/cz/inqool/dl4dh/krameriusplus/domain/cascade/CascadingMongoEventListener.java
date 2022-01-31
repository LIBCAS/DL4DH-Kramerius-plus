package cz.inqool.dl4dh.krameriusplus.domain.cascade;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.internalpart.InternalPartRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.PageRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication.PublicationRepository;
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

    private final PageRepository pageRepository;

    private final InternalPartRepository internalPartRepository;

    @Autowired
    public CascadingMongoEventListener(PublicationRepository publicationRepository,
                                       PageRepository pageRepository,
                                       InternalPartRepository internalPartRepository) {
        this.publicationRepository = publicationRepository;
        this.pageRepository = pageRepository;
        this.internalPartRepository = internalPartRepository;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Object source = event.getSource();
        ReflectionUtils.doWithFields(source.getClass(),
                new CascadeCallback(source, publicationRepository, pageRepository, internalPartRepository));
    }
}
