package cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.OwnedObject;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.OwnedObject;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlan;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a single request for enriching publications. Creates JobPlans
 */
@Getter
@Setter
@Entity
public class EnrichmentRequest extends OwnedObject {

    private String name;

    @JoinTable(name = "job_plan_enrichment_request",
            joinColumns = @JoinColumn(name = "enrichment_request_id"),
            inverseJoinColumns = @JoinColumn(name = "job_plan_id"))
    @OneToMany
    private Set<JobPlan> jobPlans = new HashSet<>();
}
