package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.chain;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DomainObject;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
@Entity
@Table(name = "kplus_enrichment_chain")
public class EnrichmentChain extends DomainObject {

    @NotNull
    private String publicationId;

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

    /**
     * Return the next jobInstance in sequence.
     * @param jobInstance
     * @return Optional of next instance to execute
     */
    public Optional<KrameriusJobInstance> getNextToExecute(KrameriusJobInstance jobInstance) {
        return Optional.of(jobs.get(jobs.entrySet().stream().dropWhile(entry -> entry.getValue().equals(jobInstance))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("JobInstance not found in chain"))
                .getKey()));
    }
}
