package cz.inqool.dl4dh.krameriusplus.service.system.export.exporter2;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;

import java.io.OutputStream;

public interface Exporter {

    void export(DigitalObject object, OutputStream outputStream);

    ExportFormat getFormat();
}
