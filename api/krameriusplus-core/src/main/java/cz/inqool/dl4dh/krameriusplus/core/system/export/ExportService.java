package cz.inqool.dl4dh.krameriusplus.core.system.export;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.ExportCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.ExportDto;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.ExportMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExportService implements DatedService<Export, ExportCreateDto, ExportDto> {

    @Getter
    private final ExportStore store;

    @Getter
    private final ExportMapper mapper;

    @Autowired
    public ExportService(ExportStore exportStore, ExportMapper exportMapper) {
        this.store = exportStore;
        this.mapper = exportMapper;
    }
}
