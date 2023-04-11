package cz.inqool.dl4dh.krameriusplus.core.job.config;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DomainObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class JobConfigView extends DomainObject {

    public abstract KrameriusJobType getJobType();
}
