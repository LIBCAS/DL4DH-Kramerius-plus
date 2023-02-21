package cz.inqool.dl4dh.krameriusplus.core.job.listener;

import cz.inqool.dl4dh.krameriusplus.api.RequestState;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.core.jms.JobEnqueueService;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.chain.EnrichmentChain;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.chain.EnrichmentChainStore;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.item.EnrichmentRequestItem;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.request.EnrichmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.api.RequestState.CREATED;
import static cz.inqool.dl4dh.krameriusplus.api.RequestState.RUNNING;
import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.*;

@Component
public class EnrichmentChainListener implements KrameriusJobListener {

    private final List<KrameriusJobType> SUPPORTED_TYPES = List.of(ENRICHMENT_EXTERNAL, ENRICHMENT_NDK, ENRICHMENT_TEI);

    private EnrichmentChainStore chainStore;

    private JobEnqueueService enqueueService;

    @Override
    @Transactional
    public void beforeJob(KrameriusJobInstance jobInstance) {
        // TODO: jobInstance might be an initialization jobInstance, in which case
        // no EnrichmentChain will be found, but the state should be changed as well

        EnrichmentChain chain = chainStore.findByKrameriusJobInstance(jobInstance);
        EnrichmentRequestItem requestItem = chain.getRequestItem();
        EnrichmentRequest request = requestItem.getEnrichmentRequest();

        if (CREATED.equals(requestItem.getState())) {
            requestItem.setState(RUNNING);
        }

        if (CREATED.equals(request.getState())) {
            request.setState(RUNNING);
        }
    }

    @Override
    @Transactional
    public void afterJob(KrameriusJobInstance jobInstance) {
        EnrichmentChain chain = chainStore.findByKrameriusJobInstance(jobInstance);

        Optional<KrameriusJobInstance> nextInstance = chain.getNextToExecute(jobInstance);

        if (nextInstance.isPresent()) {
            enqueueService.enqueue(nextInstance.get());
        } else { // after last job in chain
            propagateState(chain);
        }
    }

    private void propagateState(EnrichmentChain chain) {
        EnrichmentRequestItem requestItem = chain.getRequestItem();

        List<RequestState> requestChainsStates = requestItem.getEnrichmentChains().stream().map(EnrichmentChain::getState).collect(Collectors.toList());
        // if all chains in requestItem finished
        if (requestChainsStates.stream().allMatch(chainState -> !RUNNING.equals(chainState) && !CREATED.equals(chainState))) {
            requestItem.setState(RequestState.from(requestChainsStates));
        }

        EnrichmentRequest request = requestItem.getEnrichmentRequest();

        List<RequestState> requestItemsStates = request.getItems().stream().map(EnrichmentRequestItem::getState).collect(Collectors.toList());
        // if all items in request finished
        if (requestItemsStates.stream().allMatch(itemState -> !RUNNING.equals(itemState) && !CREATED.equals(itemState))) {
            request.setState(RequestState.from(requestItemsStates));
        }
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
