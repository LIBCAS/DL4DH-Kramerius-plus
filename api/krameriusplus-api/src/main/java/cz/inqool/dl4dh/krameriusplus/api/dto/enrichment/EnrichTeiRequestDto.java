package cz.inqool.dl4dh.krameriusplus.api.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.create.EnrichTeiCreateDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class EnrichTeiRequestDto {

    @NotEmpty
    private Set<@Valid EnrichTeiCreateDto> publications = new HashSet<>();
}
