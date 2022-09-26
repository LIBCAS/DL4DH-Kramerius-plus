package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicationDto extends ProcessingDto {

    private Publication publication;
}
