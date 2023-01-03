package cz.inqool.dl4dh.krameriusplus.api.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class JobEventFilter {

    private String publicationId;

    private Set<KrameriusJobType> krameriusJobs;

    private ExecutionStatus status;

    private boolean includeDeleted;
}
