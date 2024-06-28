package cz.inqool.dl4dh.krameriusplus.api.request.message;

import cz.inqool.dl4dh.krameriusplus.api.domain.FileRefDto;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class MessageDto {

    private String id;

    private List<FileRefDto> files;

    private String message;

    private String author;

    private Instant created;

    private Instant updated;
}
