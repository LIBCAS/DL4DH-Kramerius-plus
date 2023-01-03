package cz.inqool.dl4dh.krameriusplus.core.batch.step.writer;

import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.chain.EnrichmentChain;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.chain.EnrichmentChainStore;
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
    public void write(List<? extends List<EnrichmentChain>> items) {
        items.forEach(enrichmentChainStore::saveAll);
    }
}
