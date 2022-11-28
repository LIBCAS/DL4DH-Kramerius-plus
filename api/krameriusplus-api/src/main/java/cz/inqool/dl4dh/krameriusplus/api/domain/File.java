package cz.inqool.dl4dh.krameriusplus.api.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

@Getter
@Setter
public class File {

    private FileRefDto fileRef;

    private InputStream inputStream;
}
