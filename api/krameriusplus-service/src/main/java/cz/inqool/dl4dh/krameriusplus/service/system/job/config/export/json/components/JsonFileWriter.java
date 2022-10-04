package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.json.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.exporter.JsonExporter;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.DigitalObjectWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.FileWriter;

public abstract class JsonFileWriter<T> extends FileWriter<T> {

    protected final JsonExporter exporter;

    protected JsonFileWriter(JsonExporter exporter) {
        this.exporter = exporter;
    }

    protected String getItemFileName(DigitalObjectWithPathDto item) {
        if (item.getDigitalObject() instanceof Page) {
            return getPageFilename(((Page) item.getDigitalObject()), "json");
        } else {
            String itemClass = ((Publication) item.getDigitalObject()).getClass().getSimpleName();
            return String.format("%s_%s.json", Character.toLowerCase(itemClass.charAt(0)) + itemClass.substring(1),
                    item.getDigitalObject().getId().substring(5));
        }
    }
}
