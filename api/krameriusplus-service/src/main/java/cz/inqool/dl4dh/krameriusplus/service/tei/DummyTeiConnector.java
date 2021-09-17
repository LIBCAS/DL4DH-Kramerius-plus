package cz.inqool.dl4dh.krameriusplus.service.tei;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.export.filter.Params;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("dummy")
public class DummyTeiConnector implements TeiConnector {

    @Override
    public String convertToTeiPage(Page page) {
        return "<xml>dummy tei body</xml>";
    }

    @Override
    public String convertToTeiHeader(Publication publication) {
        return "<xml>dummy tei header</xml>";
    }

    @Override
    public String merge(String teiHeader, List<String> teiPages, Params params) {
        return "full TEI";
    }
}
