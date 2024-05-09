package cz.inqool.dl4dh.krameriusplus.api.request.file;

import cz.inqool.dl4dh.krameriusplus.api.domain.FileRefDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestFileDto {

    private String id;

    private FileRefDto fileRef;
}
