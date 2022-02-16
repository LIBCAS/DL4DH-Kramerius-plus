package cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.tei;

import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.tei.TeiConnector;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.PageDecorator;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.alto.AltoMetadataPageDecorator;
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
