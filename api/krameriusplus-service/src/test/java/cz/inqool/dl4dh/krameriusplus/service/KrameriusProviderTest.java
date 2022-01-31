package cz.inqool.dl4dh.krameriusplus.service;

import cz.inqool.dl4dh.krameriusplus.EnricherApplicationContext;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.KrameriusProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Norbert Bodnar
 */
@SpringBootTest(classes = EnricherApplicationContext.class)
public class KrameriusProviderTest {

    @Autowired
    private KrameriusProvider krameriusProvider;

    @Test
    public void testMonograph() {
        Monograph monograph = krameriusProvider.getDigitalObject("uuid:a89c5620-6b91-11eb-9d4f-005056827e52");

        System.out.println("Done");
    }
}
