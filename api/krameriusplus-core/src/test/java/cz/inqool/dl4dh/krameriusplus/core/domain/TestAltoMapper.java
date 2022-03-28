package cz.inqool.dl4dh.krameriusplus.core.domain;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.core.CoreBaseTest;
import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.PageStore;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.alto.dto.AltoDto;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.alto.dto.AltoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestAltoMapper extends CoreBaseTest {

    @Autowired
    private StreamProvider streamProvider;

    @Autowired
    private PageStore pageStore;

    @Autowired
    private AltoMapper altoMapper;

    @Test
    void testMapper() {
        Alto alto = streamProvider.getAlto("uuid:171db5c1-78df-4267-9e4d-c06be963ad7a");
        AltoDto altoDto = altoMapper.toAltoDto(alto);

        Page page = new Page();
        page.setAltoLayout(altoDto.getLayout());

        pageStore.save(page);
    }
}
