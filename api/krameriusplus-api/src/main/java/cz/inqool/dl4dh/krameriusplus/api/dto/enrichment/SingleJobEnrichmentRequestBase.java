package cz.inqool.dl4dh.krameriusplus.api.dto.enrichment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public abstract class SingleJobEnrichmentRequestBase implements SingleJobEnrichmentRequestDto {

    private String name;

    @NotEmpty
    private Set<String> publicationIds = new HashSet<>();
}
