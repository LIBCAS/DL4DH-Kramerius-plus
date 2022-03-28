package cz.inqool.dl4dh.krameriusplus.core.system.export.exporter;

import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;

public interface Exporter {

    FileRef generateFile(Publication publication);

    Export export(String publicationId, Params params);

    ExportFormat getFormat();
}
