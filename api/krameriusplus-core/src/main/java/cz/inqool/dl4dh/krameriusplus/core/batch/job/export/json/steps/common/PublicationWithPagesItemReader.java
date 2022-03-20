package cz.inqool.dl4dh.krameriusplus.core.batch.job.export.json.steps.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import lombok.NonNull;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class PublicationWithPagesItemReader implements ItemReader<Publication> {

    private final PublicationService publicationService;

    private final String publicationId;

    private final Params params;

    private boolean read = false;

    @Autowired
    public PublicationWithPagesItemReader(@Value("#{jobParameters['publicationId']}") String publicationId,
                                          @NonNull @Value("#{jobParameters['params']}") String params,
                                          PublicationService publicationService,
                                          ObjectMapper objectMapper) {
        this.publicationId = publicationId;
        this.publicationService = publicationService;

        try {
            this.params = objectMapper.readValue(params, Params.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not serialize Params"); //TODO
        }
    }

    @Override
    public Publication read() {
        if (read) {
            return null;
        }

        read = true;

        return publicationService.findWithPages(publicationId, params);
    }
}
