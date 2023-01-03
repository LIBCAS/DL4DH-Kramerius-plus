package cz.inqool.dl4dh.krameriusplus.api.export;

import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceDto;
import cz.inqool.dl4dh.krameriusplus.api.domain.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.api.domain.FileRefDto;
import cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExportDto extends DatedObjectDto {

    private String publicationId;

    private String publicationTitle;

    private FileRefDto fileRef;

    private KrameriusJobInstanceDto exportJob;

    private Long order;

    private KrameriusModel model;

    private List<ExportDto> children = new ArrayList<>();

}
