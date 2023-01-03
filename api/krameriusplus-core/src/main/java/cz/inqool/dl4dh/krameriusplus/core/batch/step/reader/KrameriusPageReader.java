package cz.inqool.dl4dh.krameriusplus.core.batch.step.reader;

import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.kramerius.KrameriusMessenger;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.LinkedList;
import java.util.stream.Collectors;

@Component
@StepScope
public class KrameriusPageReader implements ItemReader<Page> {

    private final Deque<Page> pages;

    public KrameriusPageReader(KrameriusMessenger krameriusMessenger,
                               @Value("#{jobParameters['" + JobParameterKey.PUBLICATION_ID + "']}") String publicationId) {
        this.pages = krameriusMessenger.getDigitalObjectsForParent(publicationId)
                .stream().filter(obj -> obj instanceof Page)
                .map(obj -> (Page) obj).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Page read() {
        if (pages.isEmpty()) {
            return null;
        }

        return pages.pop();
    }
}
