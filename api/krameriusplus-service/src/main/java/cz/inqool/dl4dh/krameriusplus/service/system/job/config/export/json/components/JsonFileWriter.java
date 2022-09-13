package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.json.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.exporter.JsonExporter;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.FileWriter;

public abstract class JsonFileWriter<T extends DigitalObject> extends FileWriter<T> {

    protected final JsonExporter exporter;

    protected JsonFileWriter(JsonExporter exporter) {
        this.exporter = exporter;
    }

    protected String getItemFileName(DigitalObject item) {
        if (item instanceof Page) {
            return getPageFilename((Page) item, "json");
        } else {
            String itemClass = item.getClass().getSimpleName();
            return String.format("%s_%s.json", Character.toLowerCase(itemClass.charAt(0)) + itemClass.substring(1),
                    item.getId().substring(item.getId().indexOf(':') + 1));
        }
    }
}
