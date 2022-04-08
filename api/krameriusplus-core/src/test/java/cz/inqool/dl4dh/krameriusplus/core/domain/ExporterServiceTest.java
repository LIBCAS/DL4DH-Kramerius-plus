package cz.inqool.dl4dh.krameriusplus.core.domain;

import cz.inqool.dl4dh.krameriusplus.core.CoreBaseTest;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportStore;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExporterService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExporterServiceTest extends CoreBaseTest {

    @Autowired
    private ExporterService exporterService;

    @Autowired
    private ExportStore exportStore;

    @Test
    @Transactional
    @Disabled("Cant manually set created field")
    void testListToDelete() {
        Export export = new Export();
        export.setCreated(Instant.now().minus(2, ChronoUnit.DAYS));

        exportStore.create(export);

        export = new Export();
        exportStore.create(export);

        List<Export> exportsToDelete = exporterService.listToDelete();
        assertEquals(1, exportsToDelete.size());
    }
}
