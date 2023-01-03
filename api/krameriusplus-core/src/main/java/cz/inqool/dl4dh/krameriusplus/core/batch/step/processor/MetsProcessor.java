package cz.inqool.dl4dh.krameriusplus.core.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.core.batch.step.wrapper.NdkFilePathWrappedPage;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.enricher.ndk.MetsEnricher;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class  MetsProcessor implements ItemProcessor<NdkFilePathWrappedPage, Page> {

    private MetsEnricher metsEnricher;

    @Override
    public Page process(NdkFilePathWrappedPage item) {
        Page page = item.getPage();
        page.setMetsMetadata(metsEnricher.extractMetadata(item.getNdkFilePath()));

        return page;
    }

    @Autowired
    public void setMetsEnricher(MetsEnricher metsEnricher) {
        this.metsEnricher = metsEnricher;
    }
}
