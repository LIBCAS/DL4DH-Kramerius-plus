package cz.inqool.dl4dh.krameriusplus.service.enricher.page;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
// todo use decorator pattern
public class CompletePageEnricher {

    private final List<PageEnricher> pageEnrichers;

    @Autowired
    public CompletePageEnricher(List<PageEnricher> pageEnrichers) {
        this.pageEnrichers = pageEnrichers;
    }

    public void enrich(Collection<Page> pages, EnrichmentTask task) {
        int done = 1;
        int total = pages.size();
        task.setTotalPages(total);

        for (Page page : pages) {
            task.setProcessingPage(done);
            enrichPage(page);
            task.setPercentDone(calculatePercentDone(total, done++));
        }
    }

    public void enrichPage(Page page) {
        try {
            pageEnrichers.forEach(pageEnricher -> pageEnricher.enrichPage(page));
        } catch (KrameriusException e) {
            // for example, some pages do not have ALTO and that's OK
            // todo: if none of the pages have ALTO, throw error
            log.warn("Error enriching page with ID={}, cause: {}", page.getId(), e.getMessage());
        }
    }

    //todo: this has nothing to do here, but the whole enrichmentTask progress setting kinda sucks. We need a better
    //      system to keep track of the progress (maybe JMS ?)
    private double calculatePercentDone(int total, int done) {
        return Math.round((done / (double) total) * 10000) / (double) 100;
    }
}
