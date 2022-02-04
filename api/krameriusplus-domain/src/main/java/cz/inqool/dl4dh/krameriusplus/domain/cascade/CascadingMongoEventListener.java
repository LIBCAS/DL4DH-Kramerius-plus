package cz.inqool.dl4dh.krameriusplus.domain.cascade;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.internalpart.InternalPartStore;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.PageStore;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication.PublicationStore;
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

    private final PublicationStore publicationStore;

    private final PageStore pageStore;

    private final InternalPartStore internalPartStore;

    @Autowired
    public CascadingMongoEventListener(PublicationStore publicationStore,
                                       PageStore pageStore,
                                       InternalPartStore internalPartStore) {
        this.publicationStore = publicationStore;
        this.pageStore = pageStore;
        this.internalPartStore = internalPartStore;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Object source = event.getSource();
        ReflectionUtils.doWithFields(source.getClass(),
                new CascadeCallback(source, publicationStore, pageStore, internalPartStore));
    }
}
