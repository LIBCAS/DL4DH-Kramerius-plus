package cz.inqool.dl4dh.krameriusplus.api.batch.job;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EnrichmentJobCollectionDto {

    private String publicationId;

    private List<EnrichmentJobSequenceDto> jobSequences = new ArrayList<>();
}
