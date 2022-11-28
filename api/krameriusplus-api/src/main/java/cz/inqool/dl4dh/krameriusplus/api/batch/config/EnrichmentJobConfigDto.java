package cz.inqool.dl4dh.krameriusplus.api.batch.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EnrichmentJobConfigDto extends JobConfigDto {

    private boolean override = false;

    private Integer pageErrorTolerance = 0;
}
