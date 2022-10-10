package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.text.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.DigitalObjectWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageAndAltoDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class DownloadPageAltoProcessor implements ItemProcessor<DigitalObjectWithPathDto, PageAndAltoDto> {

    private final StreamProvider streamProvider;

    @Autowired
    public DownloadPageAltoProcessor(StreamProvider streamProvider) {
        this.streamProvider = streamProvider;
    }

    @Override
    public PageAndAltoDto process(DigitalObjectWithPathDto item) throws Exception {
        if (!(item.getDigitalObject() instanceof Page)) {
            return null;
        }
        PageAndAltoDto result = new PageAndAltoDto();
        result.setDigitalObject(item.getDigitalObject());
        result.setPath(item.getPath());
        result.setAlto(streamProvider.getAlto(item.getDigitalObject().getId()));

        return result;
    }
}
