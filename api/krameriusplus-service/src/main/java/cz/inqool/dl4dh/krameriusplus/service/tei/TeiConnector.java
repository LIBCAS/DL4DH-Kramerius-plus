package cz.inqool.dl4dh.krameriusplus.service.tei;

import cz.inqool.dl4dh.krameriusplus.domain.dao.params.Params;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;

import java.io.File;
import java.util.List;

public interface TeiConnector {

    String convertToTeiPage(Page page);

    String convertToTeiHeader(Publication publication);

    File merge(String teiHeader, List<String> teiPages, Params params);
}
