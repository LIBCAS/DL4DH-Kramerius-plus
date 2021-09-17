package cz.inqool.dl4dh.krameriusplus.service.export;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.service.export.filter.Params;

import java.io.File;

public interface Exporter {

    File export(Publication publication, Params params);

    ExportFormat getFormat();
}
