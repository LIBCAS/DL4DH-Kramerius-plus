package cz.inqool.dl4dh.krameriusplus.service.export;

import cz.inqool.dl4dh.krameriusplus.domain.entity.FileRef;
import cz.inqool.dl4dh.krameriusplus.service.export.filter.Params;

public interface Exporter {

    FileRef export(String publicationId, Params params);

    ExportFormat getFormat();
}
