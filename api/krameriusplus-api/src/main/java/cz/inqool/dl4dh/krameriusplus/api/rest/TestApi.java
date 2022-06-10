package cz.inqool.dl4dh.krameriusplus.api.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.PageStore;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.monograph.MonographUnit;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.periodical.Periodical;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.periodical.PeriodicalVolume;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.WebClientDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestApi {

    private final PageStore pageStore;

    private final WebClientDataProvider dataProvider;

    @Autowired
    public TestApi(PageStore pageStore, WebClientDataProvider dataProvider) {
        this.pageStore = pageStore;
        this.dataProvider = dataProvider;
    }

    @GetMapping
    public Publication test() throws JsonProcessingException {
        Monograph monograph = new Monograph();
        Periodical periodical = new Periodical();
        MonographUnit monographUnit = new MonographUnit();
        PeriodicalVolume volume = new PeriodicalVolume();

        periodical.getChildren().add(monographUnit);
        periodical.getChildren().add(monograph);
        periodical.getChildren().add(volume);

        Publication publication = (Publication) dataProvider.getDigitalObject("uuid:587dec55-82d2-4c9b-a45c-da482e56d8e8");

        publication.getChildren().addAll(dataProvider.getDigitalObjectsForParent("uuid:587dec55-82d2-4c9b-a45c-da482e56d8e8"));

        return publication;
    }
}
