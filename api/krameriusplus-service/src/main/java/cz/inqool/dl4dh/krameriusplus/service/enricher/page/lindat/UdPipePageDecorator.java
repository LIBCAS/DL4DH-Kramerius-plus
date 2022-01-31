package cz.inqool.dl4dh.krameriusplus.service.enricher.page.lindat;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.PageDecorator;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.alto.AltoContentExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UdPipePageDecorator implements PageDecorator {

    private final UDPipeService udPipeService;

    private final AltoContentExtractor altoContentExtractor;

    @Autowired
    public UdPipePageDecorator(UDPipeService udPipeService, AltoContentExtractor altoContentExtractor) {
        this.udPipeService = udPipeService;
        this.altoContentExtractor = altoContentExtractor;
    }

    @Override
    public void enrichPage(Page page) {
        altoContentExtractor.enrichPage(page);
        udPipeService.tokenize(page);
    }
}
