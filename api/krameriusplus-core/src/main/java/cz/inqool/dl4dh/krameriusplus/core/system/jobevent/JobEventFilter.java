package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class JobEventFilter {

    private String publicationId;

    private Set<KrameriusJob> krameriusJobs;

    private JobStatus lastExecutionStatus;

    private boolean includeDeleted;
}
