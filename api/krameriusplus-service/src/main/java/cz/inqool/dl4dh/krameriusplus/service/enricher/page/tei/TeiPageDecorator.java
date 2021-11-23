package cz.inqool.dl4dh.krameriusplus.service.enricher.page.tei;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.PageDecorator;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.alto.AltoMetadataPageDecorator;
import cz.inqool.dl4dh.krameriusplus.service.tei.TeiConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeiPageDecorator implements PageDecorator {

    private final TeiConnector teiConnector;

    private final AltoMetadataPageDecorator altoMetadataPageEnricher;

    @Autowired
    public TeiPageDecorator(TeiConnector teiConnector, AltoMetadataPageDecorator altoMetadataPageEnricher) {
        this.teiConnector = teiConnector;
        this.altoMetadataPageEnricher = altoMetadataPageEnricher;
    }

    @Override
    public void enrichPage(Page page) {
        altoMetadataPageEnricher.enrichPage(page);
        page.setTeiBody(teiConnector.convertToTeiPage(page));
    }
}
