package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.DataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;

@Slf4j
@Component
@StepScope
public class DownloadDigitalObjectReader implements ItemReader<DigitalObject> {

    private final DataProvider dataProvider;

    private final String publicationId;

    private List<DigitalObject> digitalObjects;

    private int nextToReadIndex = 0;

    @Autowired
    public DownloadDigitalObjectReader(DataProvider dataProvider,
                                       @Value("#{jobParameters['" + PUBLICATION_ID + "']}") String publicationId) {
        this.dataProvider = dataProvider;
        this.publicationId = publicationId;
    }

    @Override
    public DigitalObject read() {
        if (digitalObjects == null) {
            log.debug("Fetching objects for publicationID={}", publicationId);
            digitalObjects = new ArrayList<>();
            digitalObjects.add(dataProvider.getDigitalObject(publicationId));
            findDigitalObjects(publicationId);
        }

        if (nextToReadIndex == digitalObjects.size()) {
            return null;
        }

        return digitalObjects.get(nextToReadIndex++);
    }

    private void findDigitalObjects(String publicationId) {
        List<DigitalObject> children = dataProvider.getDigitalObjectsForParent(publicationId);
        if (children.isEmpty()) {
            return;
        }

        digitalObjects.addAll(children);
        children.stream()
                .filter(object -> object instanceof Publication)
                .forEach(digitalObject -> findDigitalObjects(digitalObject.getId()));
    }
}
