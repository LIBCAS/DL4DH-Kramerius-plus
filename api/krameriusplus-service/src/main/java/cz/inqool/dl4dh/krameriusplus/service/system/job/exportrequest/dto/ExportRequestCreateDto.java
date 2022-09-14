package cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.OwnedObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export.ExportJobConfigDto;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
public abstract class ExportRequestCreateDto extends OwnedObjectCreateDto {

    private String name;

    private Set<String> publicationIds = new HashSet<>();

    public abstract ExportJobConfigDto getConfig();
}
