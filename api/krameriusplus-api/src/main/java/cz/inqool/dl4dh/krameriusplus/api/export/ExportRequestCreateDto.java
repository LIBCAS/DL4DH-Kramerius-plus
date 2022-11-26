package cz.inqool.dl4dh.krameriusplus.api.export;

import cz.inqool.dl4dh.krameriusplus.api.RequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.batch.config.ExportJobConfigDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportRequestCreateDto extends RequestCreateDto {

    private ExportJobConfigDto config;

}
