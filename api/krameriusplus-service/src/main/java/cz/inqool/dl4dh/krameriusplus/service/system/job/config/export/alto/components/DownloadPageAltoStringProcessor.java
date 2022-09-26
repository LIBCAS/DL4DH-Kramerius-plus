package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.alto.components;

import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageAndAltoStringDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.ProcessingDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class DownloadPageAltoStringProcessor implements ItemProcessor<ProcessingDto, PageAndAltoStringDto> {
    private final StreamProvider streamProvider;

    @Autowired
    public DownloadPageAltoStringProcessor(StreamProvider streamProvider) {
        this.streamProvider = streamProvider;
    }

    @Override
    public PageAndAltoStringDto process(ProcessingDto item) throws Exception {
        if (!(item instanceof PageWithPathDto)) {
            return null;
        }
        PageWithPathDto pageWithPathDto = (PageWithPathDto) item;

        PageAndAltoStringDto pageAndAltoStringDto = new PageAndAltoStringDto();
        pageAndAltoStringDto.setPage(pageWithPathDto.getPage());
        pageAndAltoStringDto.setPath(pageWithPathDto.getPath());
        pageAndAltoStringDto.setAltoString(streamProvider.getAltoString(pageAndAltoStringDto.getPage().getId()));

        return pageAndAltoStringDto;
    }
}
