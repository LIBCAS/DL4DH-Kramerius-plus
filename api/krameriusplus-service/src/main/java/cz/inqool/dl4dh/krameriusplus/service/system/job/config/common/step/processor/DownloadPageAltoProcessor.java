package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.processor;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageAndAltoDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class DownloadPageAltoProcessor implements ItemProcessor<Page, PageAndAltoDto> {
    private final StreamProvider streamProvider;

    @Autowired
    public DownloadPageAltoProcessor(StreamProvider streamProvider) {
        this.streamProvider = streamProvider;
    }

    @Override
    public PageAndAltoDto process(@NonNull Page item) throws Exception {
        return new PageAndAltoDto(item, streamProvider.getAlto(item.getId()));
    }
}
