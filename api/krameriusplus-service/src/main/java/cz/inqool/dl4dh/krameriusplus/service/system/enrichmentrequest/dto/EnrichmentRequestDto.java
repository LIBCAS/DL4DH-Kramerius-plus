package cz.inqool.dl4dh.krameriusplus.service.system.enrichmentrequest.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.OwnedObjectDto;
import cz.inqool.dl4dh.krameriusplus.service.system.jobplan.dto.JobPlanDto;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class EnrichmentRequestDto extends OwnedObjectDto {

    private String name;

    private Set<JobPlanDto> jobPlans = new HashSet<>();
}
