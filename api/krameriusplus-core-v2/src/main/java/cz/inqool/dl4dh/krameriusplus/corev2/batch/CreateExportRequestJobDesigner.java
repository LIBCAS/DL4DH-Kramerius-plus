package cz.inqool.dl4dh.krameriusplus.corev2.batch;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.validator.ExportRequestParametersValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.KrameriusJobTypeName.CREATE_EXPORT_REQUEST;
import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.*;

@Configuration
public class CreateExportRequestJobDesigner extends AbstractJobDesigner {

    private ExportRequestParametersValidator parametersValidator;

    private Step createExportItemsStep;

    private Step createExportsStep;

    private Step createMergeJobStep;

    private Step enqueueExportItemsStep;

    @Override
    public String getJobName() {
        return CREATE_EXPORT_REQUEST;
    }

    @Bean(CREATE_EXPORT_REQUEST)
    @Override
    public Job build() {
        // Step 1 - create ExportRequestItems
        //      - ItemReader - read UUIDs from ExportRequest
        //      - ItemProcessor<String, ExportRequestItem>
        //          - create ExportRequestItem from each UUID
        //      - ItemWriter - save ExportRequestItems to DB
        // Step 2 - create Exports
        //      - ItemReader - read ExportRequestItems from ExportRequest
        //      - ItemProcessor<ExportRequestItem, Export> - requestItem to Export root node processor
        //          - create Export tree (one to one tree from root publication to root Export)
        //      - ItemWriter - save Export tree (from top - down)
        // Step 3 - create MergeJob
        // Step 4 - enqueue exports
        //      - enqueue first export in every requestItem
        //      - first export in Export tree is the first node from the tree in PostOrder
        return getJobBuilder().validator(parametersValidator)
                .start(createExportItemsStep)
                .next(createExportsStep)
                .next(createMergeJobStep)
                .next(enqueueExportItemsStep)
                .build();
    }

    @Autowired
    public void setParametersValidator(ExportRequestParametersValidator parametersValidator) {
        this.parametersValidator = parametersValidator;
    }

    @Autowired
    public void setCreateExportItemsStep(@Qualifier(CREATE_EXPORT_ITEMS_STEP) Step createExportItemsStep) {
        this.createExportItemsStep = createExportItemsStep;
    }

    @Autowired
    public void setCreateExportsStep(@Qualifier(CREATE_EXPORTS_STEP) Step createExportsStep) {
        this.createExportsStep = createExportsStep;
    }

    @Autowired
    public void setCreateMergeJobStep(@Qualifier(CREATE_MERGE_JOB_STEP) Step createMergeJobStep) {
        this.createMergeJobStep = createMergeJobStep;
    }

    @Autowired
    public void setEnqueueExportItemsStep(@Qualifier(ENQUEUE_EXPORT_ITEMS_STEP) Step enqueueExportItemsStep) {
        this.enqueueExportItemsStep = enqueueExportItemsStep;
    }
}
