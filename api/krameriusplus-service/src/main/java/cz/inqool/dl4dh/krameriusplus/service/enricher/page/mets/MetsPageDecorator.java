package cz.inqool.dl4dh.krameriusplus.service.enricher.page.mets;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.PageDecorator;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.tei.TeiPageDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetsPageDecorator implements PageDecorator {

    private final TeiPageDecorator teiPageDecorator;

    @Autowired
    public MetsPageDecorator(TeiPageDecorator teiPageDecorator) {
        this.teiPageDecorator = teiPageDecorator;
    }

    @Override
    public void enrichPage(Page page) {
        teiPageDecorator.enrichPage(page);


    }
}