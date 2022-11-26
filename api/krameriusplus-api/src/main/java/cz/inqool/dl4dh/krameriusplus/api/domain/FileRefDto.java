package cz.inqool.dl4dh.krameriusplus.api.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileRefDto {

    private String name;

    private String contentType;

    private Long size;
}
