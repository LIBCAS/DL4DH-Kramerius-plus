package cz.inqool.dl4dh.krameriusplus.core.system.export;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.ExportCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.ExportDto;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.ExportMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Service
public class ExportService implements DatedService<Export, ExportCreateDto, ExportDto> {

    private final static String CRON_EVERY_DAY_AT_MIDNIGHT = "0 0 0 * * ?";

    // for debugging purposes
    private final static String CRON_EVERY_MINUTE = "0 * * * * ?";

    private final int DELETE_AFTER_HOURS = 24;

    @Getter
    private final ExportStore store;

    @Getter
    private final ExportMapper mapper;

    @Autowired
    public ExportService(ExportStore exportStore, ExportMapper exportMapper) {
        this.store = exportStore;
        this.mapper = exportMapper;
    }

    public QueryResults<Export> list(String publicationId, int page, int pageSize) {
        return store.list(publicationId, page, pageSize);
    }

    @Scheduled(cron = CRON_EVERY_DAY_AT_MIDNIGHT)
    public void cleanUp() {
        List<String> exports = listToDelete();

        exports.forEach(this::delete);
    }

    private List<String> listToDelete() {
        return store.listOlderThan(Instant.now().minus(DELETE_AFTER_HOURS, ChronoUnit.HOURS));
    }

    public ExportDto findByJobEvent(String jobEventId) {
        Export export = store.findByJobEvent(jobEventId);
        notNull(export, () -> new MissingObjectException(Export.class, "No Export for jobEventId:" + jobEventId));

        return mapper.toDto(export);
    }
}
