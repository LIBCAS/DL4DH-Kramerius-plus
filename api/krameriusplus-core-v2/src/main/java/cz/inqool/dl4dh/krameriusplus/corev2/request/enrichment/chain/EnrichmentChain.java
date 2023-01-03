package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.chain;

import cz.inqool.dl4dh.krameriusplus.api.RequestState;
import cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DomainObject;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.item.EnrichmentRequestItem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

import static cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus.*;

@Getter
@Setter
@Entity
@Table(name = "kplus_enrichment_chain")
public class EnrichmentChain extends DomainObject {

    @NotNull
    private String publicationId;

    @NotNull
    private String publicationTitle;

    @NotNull
    @Enumerated(EnumType.STRING)
    private KrameriusModel model;

    @OneToMany
    @JoinTable(
            name = "kplus_enrichment_chain_job",
            joinColumns = @JoinColumn(name = "enrichment_chain_id"),
            inverseJoinColumns = @JoinColumn(name = "kramerius_job_instance_id")
    )
    @MapKeyColumn(name = "job_order")
    private Map<Long, KrameriusJobInstance> jobs = new HashMap<>();

    @NotNull
    @Column(name = "chain_order")
    private Long order;

    @ManyToOne
    private EnrichmentRequestItem requestItem;

    /**
     * Return the next jobInstance in sequence.
     * @param jobInstance last instance
     * @return Optional of next instance to execute
     */
    public Optional<KrameriusJobInstance> getNextToExecute(KrameriusJobInstance jobInstance) {
        Iterator<KrameriusJobInstance> orderedJobs = new TreeMap<>(jobs).values().iterator();

        while (orderedJobs.hasNext()) {
            if (orderedJobs.next().equals(jobInstance)) {
                return orderedJobs.hasNext() ? Optional.of(orderedJobs.next()) : Optional.empty();
            }
        }

        return Optional.empty();
    }

    public RequestState getState() {
        if (jobs.values().stream().anyMatch(job -> STARTED.equals(job.getExecutionStatus()))) {
            return RequestState.RUNNING;
        }

        if (jobs.values().stream().allMatch(job -> FAILED.equals(job.getExecutionStatus()))) {
            return RequestState.FAILED;
        }

        if (jobs.values().stream().allMatch(job -> COMPLETED.equals(job.getExecutionStatus()))) {
            return RequestState.COMPLETED;
        }

        return RequestState.PARTIAL;
    }
}
