package cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.tei;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.tei.TeiExportParams;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public interface TeiConnector {

    String convertToTeiPage(Page page);

    String convertToTeiHeader(Publication publication);

    File merge(InputStream teiHeader, List<InputStream> teiPages, TeiExportParams params);

    File merge(InputStream teiHeader, List<InputStream> teiPages, TeiExportParams params, Path outputFile);
}
