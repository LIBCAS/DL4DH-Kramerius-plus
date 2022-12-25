package cz.inqool.dl4dh.krameriusplus.api.batch.config;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.api.domain.DomainObjectDto;

public abstract class JobConfigDto extends DomainObjectDto {

    public abstract KrameriusJobType getJobType();
}
