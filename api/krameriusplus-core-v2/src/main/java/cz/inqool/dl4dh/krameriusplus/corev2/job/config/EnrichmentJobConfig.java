package cz.inqool.dl4dh.krameriusplus.corev2.job.config;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "kplus_enrichment_job_config")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class EnrichmentJobConfig extends JobConfig {

    private boolean override = false;

    private Integer pageErrorTolerance = 0;
}
