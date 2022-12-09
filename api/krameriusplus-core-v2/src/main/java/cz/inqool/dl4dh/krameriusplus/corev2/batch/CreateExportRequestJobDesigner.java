package cz.inqool.dl4dh.krameriusplus.corev2.batch;

import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.KrameriusJobTypeName.CREATE_EXPORT_REQUEST;

@Configuration
public class CreateExportRequestJobDesigner extends AbstractJobDesigner {

    @Override
    public String getJobName() {
        return CREATE_EXPORT_REQUEST;
    }

//    @Bean(CREATE_EXPORT_REQUEST)
    @Override
    public Job build() {
        // Step 1 - create ExportRequestItems
        //      - ItemReader - read UUIDs from ExportRequest
        //      - ItemProcessor<String, ExportRequestItem> - delegate ItemProcessor
        //          - apply all ExportValidators that support exportFormat from given config
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
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }
}
