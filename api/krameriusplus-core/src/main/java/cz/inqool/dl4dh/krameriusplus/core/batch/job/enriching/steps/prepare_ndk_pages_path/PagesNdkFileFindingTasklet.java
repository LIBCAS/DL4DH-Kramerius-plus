package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.prepare_ndk_pages_path;

import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
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

@StepScope
@Component
public class PagesNdkFileFindingTasklet implements Tasklet {

    private final MetsFileFinder metsFileFinder;

    private final PublicationService publicationService;

    @Autowired
    public PagesNdkFileFindingTasklet(MetsFileFinder metsFileFinder, PublicationService publicationService) {
        this.metsFileFinder = metsFileFinder;
        this.publicationService = publicationService;
    }

    @Override
    public RepeatStatus execute(@Nullable StepContribution contribution, ChunkContext chunkContext) {
        String publicationId = (String) chunkContext.getAttribute("publicationId");

        Publication publication = publicationService.findWithPages(publicationId, new Params());

        metsFileFinder.assignMetsPathForPages(publication.getPages(), Path.of(publication.getMainMetsPath()));

        return RepeatStatus.FINISHED;
    }
}
