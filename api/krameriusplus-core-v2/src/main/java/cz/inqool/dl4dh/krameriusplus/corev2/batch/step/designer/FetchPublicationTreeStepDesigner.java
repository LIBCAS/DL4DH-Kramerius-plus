package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor.TreeBuildingProcessor;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader.EnrichmentRequestReader;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.writer.PublicationTreeWriter;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.FETCH_PUBLICATIONS;

@Component
public class FetchPublicationTreeStepDesigner extends AbstractStepDesigner {

    private final EnrichmentRequestReader enrichmentRequestReader;

    private final TreeBuildingProcessor treeBuildingProcessor;

    private final PublicationTreeWriter publicationTreeWriter;

    @Autowired
    public FetchPublicationTreeStepDesigner(EnrichmentRequestReader enrichmentRequestReader,
                                            TreeBuildingProcessor treeBuildingProcessor,
                                            PublicationTreeWriter publicationTreeWriter) {
        this.enrichmentRequestReader = enrichmentRequestReader;
        this.treeBuildingProcessor = treeBuildingProcessor;
        this.publicationTreeWriter = publicationTreeWriter;
    }

    @Override
    protected String getStepName() {
        return FETCH_PUBLICATIONS;
    }

    @Bean(FETCH_PUBLICATIONS)
    @Override
    public Step build() {
        return getStepBuilder().
                <String, Publication>chunk(5)
                .reader(enrichmentRequestReader)
                .processor(treeBuildingProcessor)
                .writer(publicationTreeWriter)
                .build();
    }
}
