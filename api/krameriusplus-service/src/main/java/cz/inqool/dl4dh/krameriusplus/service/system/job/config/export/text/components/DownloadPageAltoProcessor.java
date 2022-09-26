package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.text.components;

import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageAndAltoDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.ProcessingDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class DownloadPageAltoProcessor implements ItemProcessor<ProcessingDto, PageAndAltoDto> {

    private final StreamProvider streamProvider;

    @Autowired
    public DownloadPageAltoProcessor(StreamProvider streamProvider) {
        this.streamProvider = streamProvider;
    }

    @Override
    public PageAndAltoDto process(ProcessingDto item) throws Exception {
        if (!(item instanceof PageWithPathDto)) {
            return null;
        }

        PageWithPathDto pageWithPathDto = (PageWithPathDto) item;

        PageAndAltoDto result = new PageAndAltoDto();
        result.setPage(pageWithPathDto.getPage());
        result.setPath(pageWithPathDto.getPath());
        result.setAlto(streamProvider.getAlto(pageWithPathDto.getPage().getId()));

        return result;
    }
}
