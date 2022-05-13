package cz.inqool.dl4dh.krameriusplus.core.system.job.jobplan;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.JobEvent;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
public class JobPlan extends DatedObject {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "jobPlan")
    private Set<ScheduledJobEvent> scheduledJobEvents = new HashSet<>();

    /**
     * Returns the next jobEvent that should be executed or an empty optional if there is no job to execute
     *
     * @return JobEvent that should be executed, or {@code Optional.empty()}
     */
    public Optional<JobEvent> getNextToExecute() {
        List<ScheduledJobEvent> plannedExecutions = scheduledJobEvents.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(ScheduledJobEvent::getOrder))
                .filter(exec -> !exec.getJobEvent().wasExecuted())
                .collect(Collectors.toList());

        if (plannedExecutions.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(plannedExecutions.get(0).getJobEvent());
    }
}
