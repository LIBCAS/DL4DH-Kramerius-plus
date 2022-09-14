package cz.inqool.dl4dh.krameriusplus.api.facade;

import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.ExportRequestService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.dto.ExportRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExportFacadeImpl implements ExportFacade {

    private final FileService fileService;

    private final ExportRequestService exportRequestService;

    @Autowired
    public ExportFacadeImpl(FileService fileService,
                            ExportRequestService exportRequestService) {
        this.fileService = fileService;
        this.exportRequestService = exportRequestService;
    }

    @Override
    public ExportRequestDto export(cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.dto.ExportRequestCreateDto requestDto) {
        ExportRequestDto exportRequestDto = exportRequestService.create(requestDto);

        exportRequestService.startExecution(exportRequestDto);

        return exportRequestDto;
    }

    @Override
    public FileRef getFile(String fileRefId) {
        return fileService.find(fileRefId);
    }

    @Override
    public ExportRequestDto find(String exportRequestId) {
        return exportRequestService.find(exportRequestId);
    }

    @Override
    public List<ExportRequestDto> listAll() {
        return exportRequestService.listAll();
    }
}
