package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublishInfo;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.DataProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.ProcessingDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PublicationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;

@Component
@StepScope
@Slf4j
public class DownloadPublicationTreeReader implements ItemReader<ProcessingDto> {

    private final DataProvider dataProvider;

    private final String publicationId;

    private Deque<DigitalObject> digitalObjects;

    @Autowired
    public DownloadPublicationTreeReader(DataProvider dataProvider,
                                         @Value("#{jobParameters['" + PUBLICATION_ID + "']}") String publicationId) {
        this.dataProvider = dataProvider;
        this.publicationId = publicationId;
    }

    @Override
    public ProcessingDto read() {
        if (digitalObjects == null) {
            log.debug("Fetching objects for publicationID={}", publicationId);
            digitalObjects = new LinkedList<>();
            digitalObjects.add(dataProvider.getDigitalObject(publicationId));
        }

        return getNext();
    }

    private ProcessingDto getNext() {
        if (digitalObjects.isEmpty()) {
            return null;
        }


        DigitalObject first = digitalObjects.pop();
        ProcessingDto result = null;
        if (first instanceof Publication) {
            Publication publicationObject = (Publication) first;
            publicationObject.setPublishInfo(new PublishInfo());
            if (publicationObject.isPdf()) {
                log.warn("Publication is PDF or ePUB cannot be enriched");
                return getNext();
            }

            List<DigitalObject> children = dataProvider.getDigitalObjectsForParent(first.getId());
            long pageCount = children.stream().filter(digitalObject -> digitalObject instanceof Page).count();
            publicationObject.setPageCount(pageCount);
            children.forEach(digitalObjects::push);
            result = new PublicationDto();
            ((PublicationDto) result).setPublication(publicationObject);
        }

        if (first instanceof Page) {
            result  = new PageDto();
            ((PageDto) result).setPage((Page) first);
        }

        return result;
    }
}
