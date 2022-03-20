package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.prepare_ndk.prepare_ndk_publication_path;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.mets.MetsFileFinder;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.Optional;

@StepScope
@Component
public class PublicationNdkFileFindingTasklet implements Tasklet {

    private final PublicationService publicationService;

    private final MetsFileFinder metsFileFinder;

    @Autowired
    public PublicationNdkFileFindingTasklet(PublicationService publicationService, MetsFileFinder metsFileFinder) {
        this.publicationService = publicationService;
        this.metsFileFinder = metsFileFinder;
    }

    @Override
    public RepeatStatus execute(@Nullable StepContribution contribution, ChunkContext chunkContext) {
        String publicationId = (String) chunkContext.getAttribute("publicationId");

        Optional<Path> mainMetsPath = metsFileFinder.getMainMetsPath(publicationId);

        if (mainMetsPath.isPresent()) {
            // TODO: partially update only the desired field without
            // fetching the whole document
            Publication publication = publicationService.find(publicationId);
            publication.setMainMetsPath(mainMetsPath.get().toString());

            publicationService.save(publication);

            return RepeatStatus.FINISHED;
        }

        throw new IllegalStateException("NDK directory not found for publication " + publicationId);
    }
}
