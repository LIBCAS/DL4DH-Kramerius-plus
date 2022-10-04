package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.json.components;

import cz.inqool.dl4dh.krameriusplus.service.system.exporter.JsonExporter;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PathedDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PublicationWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.FileWriter;

public abstract class JsonFileWriter<T> extends FileWriter<T> {

    protected final JsonExporter exporter;

    protected JsonFileWriter(JsonExporter exporter) {
        this.exporter = exporter;
    }

    protected String getItemFileName(PathedDto item) {
        if (item instanceof PageWithPathDto) {
            return getPageFilename(((PageWithPathDto) item).getPage(), "json");
        } else {
            String itemClass = ((PublicationWithPathDto) item).getPublication().getClass().getSimpleName();
            return String.format("%s_%s.json", Character.toLowerCase(itemClass.charAt(0)) + itemClass.substring(1),
                    ((PublicationWithPathDto) item).getPublication().getId().substring(5));
        }
    }
}
