package cz.inqool.dl4dh.krameriusplus.core.job.listener;

import cz.inqool.dl4dh.krameriusplus.api.RequestState;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.jms.JobEnqueueService;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstanceStore;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.chain.EnrichmentChain;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.chain.EnrichmentChainStore;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.item.EnrichmentRequestItem;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.request.EnrichmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.api.RequestState.*;
import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.*;

@RequiredArgsConstructor
@Component
public class EnrichmentChainListener implements KrameriusJobListener {

    private final List<KrameriusJobType> SUPPORTED_TYPES = List.of(ENRICHMENT_EXTERNAL, ENRICHMENT_NDK, ENRICHMENT_TEI);

    private final EnrichmentChainStore chainStore;

    private final KrameriusJobInstanceStore jobInstanceStore;

    private final JobEnqueueService enqueueService;

    @Override
    @Transactional
    public void beforeJob(String jobInstanceId) {
        KrameriusJobInstance jobInstance = jobInstanceStore.findById(jobInstanceId)
                .orElseThrow(() -> new MissingObjectException(KrameriusJobInstance.class, jobInstanceId));

        EnrichmentChain chain = chainStore.findByKrameriusJobInstance(jobInstance);
        EnrichmentRequestItem requestItem = chain.getRequestItem();
        EnrichmentRequest request = requestItem.getEnrichmentRequest();

        requestItem.setState(RUNNING);
        request.setState(RUNNING);
    }

    @Override
    @Transactional
    public void afterJob(String jobInstanceId) {
        KrameriusJobInstance jobInstance = jobInstanceStore.findById(jobInstanceId)
                .orElseThrow(() -> new MissingObjectException(KrameriusJobInstance.class, jobInstanceId));

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
        EnrichmentRequest request = requestItem.getEnrichmentRequest();
        if (request.getState().equals(CANCELLED)) {
            requestItem.setState(CANCELLED);
            return;
        }

        List<RequestState> requestChainsStates = requestItem.getEnrichmentChains().stream().map(EnrichmentChain::getState).collect(Collectors.toList());
        // if all chains in requestItem finished
        if (requestChainsStates.stream().allMatch(chainState -> !RUNNING.equals(chainState) && !CREATED.equals(chainState))) {
            requestItem.setState(RequestState.from(requestChainsStates));
        }


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
}
