package cz.inqool.dl4dh.krameriusplus.corev2.job.listener;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.corev2.jms.JobEnqueueService;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.chain.EnrichmentChain;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.chain.EnrichmentChainStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.*;

@Component
public class EnrichmentChainListener implements KrameriusJobListener {

    private final List<KrameriusJobType> SUPPORTED_TYPES = List.of(ENRICHMENT_EXTERNAL, ENRICHMENT_NDK, ENRICHMENT_TEI);

    private EnrichmentChainStore chainStore;

    private JobEnqueueService enqueueService;

    @Override
    @Transactional
    public void afterJob(KrameriusJobInstance jobInstance) {
        EnrichmentChain chain = chainStore.findByKrameriusJobInstance(jobInstance);

        Optional<KrameriusJobInstance> nextInstance = chain.getNextToExecute(jobInstance);

        nextInstance.ifPresent(krameriusJobInstance -> enqueueService.enqueue(krameriusJobInstance));
    }

    @Override
    public boolean supports(KrameriusJobInstance jobInstance) {
        return SUPPORTED_TYPES.contains(jobInstance.getJobType());
    }

    @Autowired
    public void setChainStore(EnrichmentChainStore chainStore) {
        this.chainStore = chainStore;
    }

    @Autowired
    public void setEnqueueService(JobEnqueueService enqueueService) {
        this.enqueueService = enqueueService;
    }
}
