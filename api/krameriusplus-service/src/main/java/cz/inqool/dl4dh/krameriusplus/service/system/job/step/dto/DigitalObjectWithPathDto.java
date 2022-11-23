package cz.inqool.dl4dh.krameriusplus.service.system.job.step.dto;


import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DigitalObjectWithPathDto {

    private DigitalObject digitalObject;

    private String path;
}
