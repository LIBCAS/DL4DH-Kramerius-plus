package cz.inqool.dl4dh.krameriusplus.corev2.request.export.request;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportFacade;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestDto;
import org.springframework.stereotype.Service;

@Service
public class ExportRequestFacade implements ExportFacade {

    @Override
    public ExportRequestDto export(ExportRequestCreateDto requestDto) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public ExportRequestDto find(String exportRequestId) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public Result<ExportRequestDto> list(String name, String owner, Boolean isFinished, int page, int pageSize) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }
}
