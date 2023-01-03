package cz.inqool.dl4dh.krameriusplus.api.export;

import cz.inqool.dl4dh.krameriusplus.api.domain.DomainObjectDto;
import cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportRequestItemDto extends DomainObjectDto {

    private String publicationId;

    private String publicationTitle;

    private Long order;

    private KrameriusModel model;

    private ExportDto rootExport;
}
