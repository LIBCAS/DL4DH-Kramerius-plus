package cz.inqool.dl4dh.krameriusplus.api.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileRefDto extends DatedObjectDto {

    private String name;

    private String contentType;

    private Long size;
}
