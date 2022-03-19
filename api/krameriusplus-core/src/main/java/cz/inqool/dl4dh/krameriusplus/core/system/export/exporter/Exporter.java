package cz.inqool.dl4dh.krameriusplus.core.system.export.exporter;

import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;

public interface Exporter {

    Export export(String publicationId, Params params);

    ExportFormat getFormat();
}
