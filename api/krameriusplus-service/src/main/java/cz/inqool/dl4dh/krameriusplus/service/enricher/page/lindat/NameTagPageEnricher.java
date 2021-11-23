package cz.inqool.dl4dh.krameriusplus.service.enricher.page.lindat;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.enricher.NameTagService;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.PageEnricher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(2)
public class NameTagPageEnricher implements PageEnricher {

    private final NameTagService nameTagService;

    @Autowired
    public NameTagPageEnricher(NameTagService nameTagService) {
        this.nameTagService = nameTagService;
    }

    @Override
    public void enrichPage(Page page) {
        nameTagService.processTokens(page);
    }
}
