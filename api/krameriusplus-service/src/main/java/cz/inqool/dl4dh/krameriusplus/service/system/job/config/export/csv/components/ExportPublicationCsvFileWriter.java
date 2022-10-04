package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.csv.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PublicationWithPathDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.eq;

@Component
@StepScope
public class ExportPublicationCsvFileWriter extends CsvFileWriter<Publication> {

    @Override
    public void write(List<? extends Publication> items) throws Exception {
        eq(items.size(), 1, () -> new IllegalStateException("Expected one publication on write, got: " + items.size()));

        Publication publication = items.get(0);
        PublicationWithPathDto publicationWithPathDto = new PublicationWithPathDto();
        publicationWithPathDto.setPublication(publication);
        publicationWithPathDto.setPath(findPublicationDirectory(publication));
        exporter.export(publication, getItemOutputStream(publicationWithPathDto));
    }
}
