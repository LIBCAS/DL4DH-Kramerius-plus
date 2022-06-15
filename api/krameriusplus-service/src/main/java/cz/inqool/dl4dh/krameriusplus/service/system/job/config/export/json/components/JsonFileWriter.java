package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.json.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.service.system.exporter.JsonExporter;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.FileWriter;

public abstract class JsonFileWriter<T extends DigitalObject> extends FileWriter<T> {

    protected final JsonExporter exporter;

    protected final ExportFormat exportFormat = ExportFormat.JSON;

    protected JsonFileWriter(JsonExporter exporter) {
        this.exporter = exporter;
    }

    protected String getItemFileName(DigitalObject item) {
        String itemClassName = item.getClass().getSimpleName();
        String suffix = Character.toLowerCase(itemClassName.charAt(0)) + itemClassName.substring(1);

        return exportFormat.getFileName(suffix, item.getId());
    }
}
