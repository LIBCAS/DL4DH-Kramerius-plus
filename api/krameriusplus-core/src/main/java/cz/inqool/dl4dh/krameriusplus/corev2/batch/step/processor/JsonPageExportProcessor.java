package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper.DigitalObjectExport;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class JsonPageExportProcessor implements ItemProcessor<Page, DigitalObjectExport> {

    private ObjectMapper objectMapper;

    @Override
    public DigitalObjectExport process(Page item) throws Exception {
        item.setTeiBodyFileId(null);

        return new DigitalObjectExport(item, objectMapper.writeValueAsString(item),
                "page_" + item.getId().substring(5) + ".json");
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
