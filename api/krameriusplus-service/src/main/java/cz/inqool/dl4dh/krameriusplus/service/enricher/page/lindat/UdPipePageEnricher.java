package cz.inqool.dl4dh.krameriusplus.service.enricher.page.lindat;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.enricher.UDPipeService;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.PageEnricher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(1)
public class UdPipePageEnricher implements PageEnricher {

    private final UDPipeService udPipeService;

    @Autowired
    public UdPipePageEnricher(UDPipeService udPipeService) {
        this.udPipeService = udPipeService;
    }

    @Override
    public void enrichPage(Page page) {
        udPipeService.tokenize(page);
    }
}
