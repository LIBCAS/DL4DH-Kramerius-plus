package cz.inqool.dl4dh.krameriusplus.service.enricher.page.lindat;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.PageDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NameTagPageDecorator implements PageDecorator {

    private final NameTagService nameTagService;

    private final UdPipePageDecorator udPipePageEnricher;

    @Autowired
    public NameTagPageDecorator(NameTagService nameTagService, UdPipePageDecorator udPipePageEnricher) {
        this.nameTagService = nameTagService;
        this.udPipePageEnricher = udPipePageEnricher;
    }

    @Override
    public void enrichPage(Page page) {
        udPipePageEnricher.enrichPage(page);
        nameTagService.processTokens(page);
    }
}
