package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.DataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
@Slf4j
public class KrameriusPublicationChildrenReader implements ItemReader<DigitalObject> {

    private final DataProvider dataProvider;

    private final String publicationId;

    private List<DigitalObject> digitalObjects;

    private int nextToReadIndex = 0;

    @Autowired
    public KrameriusPublicationChildrenReader(DataProvider dataProvider,
                                              @Value("#{jobParameters['publicationId']}") String publicationId) {
        this.dataProvider = dataProvider;
        this.publicationId = publicationId;
    }

    @Override
    public DigitalObject read() {
        if (digitalObjects == null) {
            log.debug("Downloading children for publication with ID={}", publicationId);
            digitalObjects = dataProvider.getDigitalObjectsForParent(publicationId);
        }

        if (nextToReadIndex == digitalObjects.size()) {
            return null;
        }

        return digitalObjects.get(nextToReadIndex++);
    }
}
