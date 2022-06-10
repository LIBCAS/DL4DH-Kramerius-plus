package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.batched.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.service.system.export.exporter2.ExporterMediator2;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.EXPORT_FORMAT;

@Component
@StepScope
public class PageExportFileWriter extends DigitalObjectExportFileWriter<Page> {

    @Autowired
    public PageExportFileWriter(ExporterMediator2 exporterMediator, @Value("#{jobParameters['" + EXPORT_FORMAT + "']}") String exportFormat) {
        super(exporterMediator, ExportFormat.fromString(exportFormat));
    }
}
