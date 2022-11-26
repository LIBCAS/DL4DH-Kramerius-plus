package cz.inqool.dl4dh.krameriusplus.api.batch.job;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EnrichmentJobSequenceDto {

    private String publicationId;

    private List<EnrichmentJobDto> jobs = new ArrayList<>();
}
