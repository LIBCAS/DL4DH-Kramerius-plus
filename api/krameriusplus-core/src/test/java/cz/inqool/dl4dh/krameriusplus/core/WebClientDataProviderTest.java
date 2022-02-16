package cz.inqool.dl4dh.krameriusplus.core;

import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius.WebClientDataProvider;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.periodical.Periodical;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class WebClientDataProviderTest extends CoreBaseTest {

    @Autowired
    private WebClientDataProvider webClientDataProvider;

    @Test
    public void testPeriodical() {
        String periodicalId = "uuid:319546a0-5a42-11eb-b4d1-005056827e51";

        DigitalObject digitalObject = webClientDataProvider.getDigitalObject(periodicalId);

        assertTrue(digitalObject instanceof Periodical);
        assertEquals("Protokol ... veřejné schůze bratrstva sv. Michala v Praze dne", ((Periodical) digitalObject).getTitle());
    }
}
