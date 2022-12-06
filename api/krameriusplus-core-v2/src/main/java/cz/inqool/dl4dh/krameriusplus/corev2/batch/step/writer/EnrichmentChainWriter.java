package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.writer;

import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.chain.EnrichmentChain;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.chain.EnrichmentChainStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class EnrichmentChainWriter implements ItemWriter<List<EnrichmentChain>> {

    private final EnrichmentChainStore enrichmentChainStore;

    @Autowired
    public EnrichmentChainWriter(EnrichmentChainStore enrichmentChainStore) {
        this.enrichmentChainStore = enrichmentChainStore;
    }

    @Override
    public void write(List<? extends List<EnrichmentChain>> items) throws Exception {
        items.forEach(enrichmentChainStore::create);
    }
}
