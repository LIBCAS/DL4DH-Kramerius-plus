package cz.inqool.dl4dh.krameriusplus.core.system.enricher.page;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.mets.MetsPageDecorator;
import cz.inqool.dl4dh.krameriusplus.core.system.scheduling.EnrichmentTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class CompletePageEnricher {

    private final MetsPageDecorator pageEnricher;

    @Autowired
    public CompletePageEnricher(MetsPageDecorator metsPageDecorator) {
        this.pageEnricher = metsPageDecorator;
    }

    public void enrich(Collection<Page> pages, EnrichmentTask.EnrichmentSubTask task) {
        if (pages == null || pages.isEmpty()) {
            return;
        }

        int done = 1;
        int errorCount = 0;
        int total = pages.size();
        task.setTotalPages(total);

        for (Page page : pages) {
            task.setCurrentPage(done++);

            try {
                enrichPage(page);
            } catch (KrameriusException e) {
                // for example, some pages do not have ALTO and that's OK
                errorCount++;
                log.warn("Error enriching page with ID={}, cause: {}", page.getId(), e.getMessage());
            }
        }

        if (errorCount == total) {
            throw new KrameriusException(KrameriusException.ErrorCode.NO_PAGE_HAD_ALTO, "Not even a single page had ALTO format, so no page was enriched.");
        }
    }

    public void enrichPage(Page page) {
        pageEnricher.enrichPage(page);
    }
}
