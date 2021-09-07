package cz.inqool.dl4dh.krameriusplus.service.enricher.tei;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;

@Service
public class WebClientTeiConnector implements TeiConnector {

    private WebClient webClient;

    @Override
    public String convertToTeiPage(Page page) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String convertToTeiHeader(Publication publication) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Resource(name = "teiWebClient")
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }
}
