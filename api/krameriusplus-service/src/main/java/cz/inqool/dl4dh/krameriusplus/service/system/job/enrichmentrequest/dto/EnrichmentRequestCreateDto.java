package cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.DatedObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanCreateDto;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class EnrichmentRequestCreateDto extends DatedObjectCreateDto {

    private String name;

    private Set<JobPlanCreateDto> jobPlans = new HashSet<>();
}
