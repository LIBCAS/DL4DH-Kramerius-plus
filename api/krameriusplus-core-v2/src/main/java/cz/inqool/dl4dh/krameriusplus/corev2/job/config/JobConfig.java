package cz.inqool.dl4dh.krameriusplus.corev2.job.config;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DomainObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class JobConfig extends DomainObject {

    public abstract KrameriusJobType getJobType();
}
