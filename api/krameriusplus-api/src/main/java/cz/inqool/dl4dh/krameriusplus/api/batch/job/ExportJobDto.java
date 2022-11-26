package cz.inqool.dl4dh.krameriusplus.api.batch.job;

import cz.inqool.dl4dh.krameriusplus.api.domain.FileRefDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExportJobDto {

    private String publicationId;

    private String publicationTitle;

    private FileRefDto fileRef;

    private List<JobExecutionDto> executions = new ArrayList<>();

}
