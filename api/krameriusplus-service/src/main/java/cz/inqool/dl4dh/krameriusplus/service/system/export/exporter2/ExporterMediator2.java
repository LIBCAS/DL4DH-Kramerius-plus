package cz.inqool.dl4dh.krameriusplus.service.system.export.exporter2;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Service
public class ExporterMediator2 {

    private final Map<ExportFormat, Exporter> exporters = new HashMap<>();

    @Autowired
    public ExporterMediator2(List<Exporter> exporters) {
        exporters.forEach(exporter -> this.exporters.put(exporter.getFormat(), exporter));
    }

    public void export(OutputStream destination, DigitalObject digitalObject, ExportFormat format) {
        Exporter exporter = exporters.get(format);
        notNull(exporter, () -> new IllegalArgumentException("No exporter for format '" + format + "'."));

        exporter.export(digitalObject, destination);
        // throw error
    }

}
