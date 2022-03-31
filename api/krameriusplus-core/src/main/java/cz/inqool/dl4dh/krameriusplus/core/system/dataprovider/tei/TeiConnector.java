package cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.tei;

import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.TeiParams;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface TeiConnector {

    String convertToTeiPage(Page page);

    String convertToTeiHeader(Publication publication);

    File merge(InputStream teiHeader, List<InputStream> teiPages, TeiParams params);
}
