package cz.inqool.dl4dh.krameriusplus.api.export;

import cz.inqool.dl4dh.krameriusplus.api.domain.FileRefDto;

import java.io.InputStream;

public interface FileFacade {

    FileRefDto getFile(String id);

    InputStream getFileContent(String id);
}
