package cz.inqool.dl4dh.krameriusplus.core.file;

import cz.inqool.dl4dh.krameriusplus.api.domain.FileRefDto;
import cz.inqool.dl4dh.krameriusplus.api.export.FileFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class FileRefFacade implements FileFacade {

    private FileRefMapper mapper;

    private FileService service;

    @Override
    public FileRefDto getFile(String id) {
        return mapper.toDto(service.find(id));
    }

    @Override
    public InputStream getFileContent(String id) {
        return service.find(id).open();
    }

    @Autowired
    public void setMapper(FileRefMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setService(FileService service) {
        this.service = service;
    }
}
