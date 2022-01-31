package cz.inqool.dl4dh.krameriusplus.service.enricher.page.mets;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.PageDecorator;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.tei.TeiPageDecorator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MetsPageDecorator implements PageDecorator {

    private final TeiPageDecorator teiPageDecorator;

    private final MetsExtractor metsExtractor;

    @Autowired
    public MetsPageDecorator(TeiPageDecorator teiPageDecorator, MetsExtractor metsExtractor) {
        this.teiPageDecorator = teiPageDecorator;
        this.metsExtractor = metsExtractor;
    }

    @Override
    public void enrichPage(Page page) {
        teiPageDecorator.enrichPage(page);

        if (page.getMetsPath() != null) {
            metsExtractor.enrich(page);
        }
    }
}
