package cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.tei;

import cz.inqool.dl4dh.krameriusplus.core.domain.params.TeiParams;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;

import java.io.File;
import java.util.List;

public interface TeiConnector {

    String convertToTeiPage(Page page);

    String convertToTeiHeader(Publication publication);

    File merge(String teiHeader, List<String> teiPages, TeiParams params);
}
