package cz.inqool.dl4dh.krameriusplus.service.enricher.page.tei;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.PageEnricher;
import cz.inqool.dl4dh.krameriusplus.service.tei.TeiConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(4)
public class TeiPageEnricher implements PageEnricher {

    private final TeiConnector teiConnector;

    @Autowired
    public TeiPageEnricher(TeiConnector teiConnector) {
        this.teiConnector = teiConnector;
    }

    @Override
    public void enrichPage(Page page) {
        page.setTeiBody(teiConnector.convertToTeiPage(page));
    }
}
