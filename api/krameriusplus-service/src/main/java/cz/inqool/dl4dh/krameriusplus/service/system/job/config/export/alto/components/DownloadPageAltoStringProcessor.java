package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.alto.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.DigitalObjectWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageAndAltoStringDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Slf4j
public class DownloadPageAltoStringProcessor implements ItemProcessor<DigitalObjectWithPathDto, PageAndAltoStringDto> {
    private final StreamProvider streamProvider;

    @Autowired
    public DownloadPageAltoStringProcessor(StreamProvider streamProvider) {
        this.streamProvider = streamProvider;
    }

    @Override
    public PageAndAltoStringDto process(DigitalObjectWithPathDto item) throws Exception {
        if (!(item.getDigitalObject() instanceof Page)) {
            return null;
        }
        PageAndAltoStringDto pageAndAltoStringDto = new PageAndAltoStringDto();
        pageAndAltoStringDto.setDigitalObject(item.getDigitalObject());
        pageAndAltoStringDto.setPath(item.getPath());
        pageAndAltoStringDto.setAltoString(streamProvider.getAltoString(pageAndAltoStringDto.getDigitalObject().getId()));

        return pageAndAltoStringDto;
    }
}
