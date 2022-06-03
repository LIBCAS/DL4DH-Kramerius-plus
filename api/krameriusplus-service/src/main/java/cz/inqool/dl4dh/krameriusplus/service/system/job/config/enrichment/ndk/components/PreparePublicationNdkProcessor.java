package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.MetsFileFinder;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Optional;

@Component
@StepScope
public class PreparePublicationNdkProcessor implements ItemProcessor<Publication, Publication> {

    private final MetsFileFinder metsFileFinder;

    @Autowired
    public PreparePublicationNdkProcessor(MetsFileFinder metsFileFinder) {
        this.metsFileFinder = metsFileFinder;
    }

    @Override
    public Publication process(Publication item) throws Exception {
        Optional<Path> mainMetsPath = metsFileFinder.findNdkPublicationDirectory(item.getId());

        if (mainMetsPath.isPresent()) {
            item.setNdkDirPath(mainMetsPath.get().toString());

            return item;
        }

        throw new IllegalStateException("NDK directory not found for publication " + item.getId());
    }
}
