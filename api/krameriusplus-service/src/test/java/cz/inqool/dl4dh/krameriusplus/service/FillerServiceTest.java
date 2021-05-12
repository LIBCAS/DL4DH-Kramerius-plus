package cz.inqool.dl4dh.krameriusplus.service;

import cz.inqool.dl4dh.krameriusplus.EnricherApplicationContext;
import cz.inqool.dl4dh.krameriusplus.service.filler.FillerService;
import cz.inqool.dl4dh.krameriusplus.service.filler.PublicationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Norbert Bodnar
 */
@SpringBootTest(classes = EnricherApplicationContext.class)
@Slf4j
public class FillerServiceTest {

    @Autowired
    private FillerService fillerService;

    @Autowired
    private PublicationService publicationService;

    @Test
    public void testBulk() {
        //bulk operation of every page at once was slower
//        String pid = "uuid:b4223240-9c31-11e7-ae0a-005056827e52";
//
//        long start = System.currentTimeMillis();
//        fillerService.enrichPublication(pid);
//        long executionTime = System.currentTimeMillis() - start;
//        log.info("Bulk processing executed in " + executionTime + "ms");
//
//        start = System.currentTimeMillis();
//        fillerService.enrichPublication(pid);
//        executionTime = System.currentTimeMillis() - start;
//        log.info("Non-Bulk processing executed in " + executionTime + "ms");
//
//        Monograph monograph = publicationService.findMonograph(pid, true);
//
//        assertEquals(208, monograph.getPages().size());
    }
}
