package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter.Filter;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.utils.JsonUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PARAMS;

@Component
@StepScope
public class FilteringProcessor implements ItemProcessor<DigitalObject, DigitalObject> {

    private final Params params;
    
    @Autowired
    public FilteringProcessor(@Value("#{jobParameters['" + PARAMS + "']}") String stringParams) {
        if (stringParams != null) {
            this.params = JsonUtils.fromJsonString(stringParams, Params.class);
        } else {
            this.params = new Params();
        }
    }

    @Override
    public DigitalObject process(DigitalObject item) throws Exception {
        for (Filter filter : params.getFilters()) {
            if (!filter.eval(item)) {
                return null;
            }
        }

        return item;
    }
}
