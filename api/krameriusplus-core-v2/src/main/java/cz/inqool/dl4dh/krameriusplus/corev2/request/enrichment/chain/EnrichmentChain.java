package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.chain;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DomainObject;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
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
    private Map<Long, KrameriusJobInstance> jobs = new HashMap<>();

    @NotNull
    private Long order;

    /**
     * Return the next jobInstance in sequence.
     * @param jobInstance
     * @return
     */
    public Optional<KrameriusJobInstance> getNextToExecute(KrameriusJobInstance jobInstance) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
}
