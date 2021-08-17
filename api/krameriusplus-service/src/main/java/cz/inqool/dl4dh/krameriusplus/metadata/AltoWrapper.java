package cz.inqool.dl4dh.krameriusplus.metadata;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;

/**
 * Wrapper class for {@code Alto} (generated source) to perform tasks on this format, such
 * as extracting plain text or enriching page with metadata from ALTO
 */
public class AltoWrapper {

    private final AltoContentExtractor contentExtractor;
    private final AltoPageEnricher pageEnricher;

    public AltoWrapper(Alto alto) {
        this.contentExtractor = new AltoContentExtractor(alto);
        this.pageEnricher = new AltoPageEnricher(alto);
    }

    public String extractPageContent() {
        return contentExtractor.extractPageContent();
    }

    public void enrichPage(Page page) {
        pageEnricher.enrichWithAlto(page);
    }
}
