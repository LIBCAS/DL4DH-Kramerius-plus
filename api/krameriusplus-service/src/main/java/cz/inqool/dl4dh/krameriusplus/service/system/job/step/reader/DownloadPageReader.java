package cz.inqool.dl4dh.krameriusplus.service.system.job.step.reader;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.DataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;

@Component
@StepScope
@Slf4j
public class DownloadPageReader implements ItemReader<Page> {
    private final DataProvider dataProvider;

    private final String publicationId;

    private List<DigitalObject> digitalObjects;

    private int nextToReadIndex = 0;

    @Autowired
    public DownloadPageReader(DataProvider dataProvider,
                              @Value("#{jobParameters['" + PUBLICATION_ID + "']}") String publicationId) {
        this.dataProvider = dataProvider;
        this.publicationId = publicationId;
    }

    @Override
    public Page read() {
        if (digitalObjects == null) {
            log.debug("Downloading children for publication with ID={}", publicationId);
            digitalObjects = dataProvider.getDigitalObjectsForParent(publicationId);
        }

        if (nextToReadIndex == digitalObjects.size()) {
            return null;
        }
        DigitalObject digitalObject = digitalObjects.get(nextToReadIndex++);

        if (!(digitalObject instanceof Page)) {
            return null;
        }

        return (Page) digitalObject;
    }
}