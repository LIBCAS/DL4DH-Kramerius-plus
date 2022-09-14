package cz.inqool.dl4dh.krameriusplus.core.system.bulkexport;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.core.system.bulkexport.dto.BulkExportCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.bulkexport.dto.BulkExportDto;
import cz.inqool.dl4dh.krameriusplus.core.system.bulkexport.dto.BulkExportMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BulkExportService implements DatedService<BulkExport, BulkExportCreateDto, BulkExportDto> {

    @Getter
    private final BulkExportMapper mapper;

    @Getter
    private final BulkExportStore store;

    @Autowired
    public BulkExportService(BulkExportMapper mapper, BulkExportStore store) {
        this.mapper = mapper;
        this.store = store;
    }
}
