package cz.inqool.dl4dh.krameriusplus.service.export;

import cz.inqool.dl4dh.krameriusplus.domain.entity.export.Export;
import cz.inqool.dl4dh.krameriusplus.service.export.filter.Params;

public interface Exporter {

    Export export(String publicationId, Params params);

    ExportFormat getFormat();
}