package cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanDto;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class EnrichmentRequestDto extends DatedObjectDto {

    private String name;

    private Set<JobPlanDto> jobPlans = new HashSet<>();
}
