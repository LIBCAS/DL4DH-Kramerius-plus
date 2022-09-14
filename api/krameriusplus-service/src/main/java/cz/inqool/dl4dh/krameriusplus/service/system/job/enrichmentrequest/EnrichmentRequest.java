package cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.OwnedObject;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlan;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a single request for enriching multiple publications with possibly multiple jobs for each.
 */
@Getter
@Setter
@Entity
public class EnrichmentRequest extends OwnedObject {

    /**
     * Optional name for the request
     */
    private String name;

    /**
     * Set of JobPlans. Each jobPlan consists of a sequence of jobEvents for one publication. When execution is started,
     * each publication's jobs (jobPlan) are ran in parallel
     */
    @JoinTable(name = "enrichment_request_job_plans",
            joinColumns = @JoinColumn(name = "enrichment_request_id"),
            inverseJoinColumns = @JoinColumn(name = "job_plan_id"))
    @OneToMany(fetch = FetchType.EAGER)
    private Set<JobPlan> jobPlans = new HashSet<>();
}
