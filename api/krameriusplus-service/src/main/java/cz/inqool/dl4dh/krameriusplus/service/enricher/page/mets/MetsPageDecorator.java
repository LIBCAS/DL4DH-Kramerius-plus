package cz.inqool.dl4dh.krameriusplus.service.enricher.page.mets;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.PageDecorator;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.tei.TeiPageDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class MetsPageDecorator implements PageDecorator {

    private final TeiPageDecorator teiPageDecorator;

    private final MetsExtractor metsExtractor;

    private final MetsFileFinder metsFinder;

    @Autowired
    public MetsPageDecorator(TeiPageDecorator teiPageDecorator, MetsExtractor metsExtractor, MetsFileFinder metsFinder) {
        this.teiPageDecorator = teiPageDecorator;
        this.metsExtractor = metsExtractor;
        this.metsFinder = metsFinder;
    }

    @Override
    public void enrichPage(Page page) {
        teiPageDecorator.enrichPage(page);

        Path pageMets = metsFinder.findMetsForPage(page);

        metsExtractor.enrich(pageMets, page);
    }
}
