package cz.inqool.dl4dh.krameriusplus.service;

import cz.inqool.dl4dh.krameriusplus.EnricherApplicationContext;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.alto.AltoContentExtractor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = EnricherApplicationContext.class)
public class AltoContentExtractorTest {

    @Autowired
    private AltoContentExtractor altoContentExtractor;

    @Test
    public void testExtractStringFromAlto() {
        Page page = new Page();
        page.setId("uuid:189d4d39-8862-4bb8-9e51-b7d914a4087a");

        altoContentExtractor.enrichPage(page);

        String actual = page.getContent();

        String expected = "Osmnáctá valná hromada bratrstva sv. Michaela pro království České, konána byla dne 16. dubna 1898 ve velkém sále Rudolfina a sice vzhledem к šedesátiletému jubileu diamantovému Jeho Svatosti papeže Lva XIIL, jakož i к padesátiletému jubileu panovnickému Jeho Veličenstva císaře a krále Františka Josefa I. způsobem zvlášt slavnostním a manifestačníra. — Průběh slavnostního tohoto shromáždění, jemuž zvláštního lesku dodala přítomnost Jeho Eminencí nejdùst. pana kardinála knížete-arcibiskupa Františka de Paula hraběte ze Schonbornů, a okázale zastoupená vysoká šlechta, byl v každém ohledu důstojný; schůze byla četně navštivena a obeslána zástupci duchovenstva všech vrstev, světským i řeholním, jakož i deputacemi všech katolických spolků pražských. Prostorný sál Rudolfina vyzdoben byl vkusně květinami a koberci a slavnostně osvětlen. V. háji exotických květin skvěl se v čele nad sedadly hodnostářů velký obraz Jeho Svatosti, pod nímž poprsí Jeho Veličenstva krásnou výzdobu doplňovalo. Dle programu zahájiti měl slavnostní valnou hromadu, kteráž o 7. hodině večer počala, Jeho Jasnost, Karel princ ze Schwarzen- bergů, jako předseda bratrstva, jelikož však pro nemoc shromáždění toho vůbec súčastniti se nemohl, převzal předsednictví vdp. kníž. arcib. vikář Thlg. Dr. Frant. Hrádek, místopředseda bratrstva a zahájil po příchodu Jeho Eminencí nejdùst. pana kardinála, jenž povstáním ze sedadel uvítán byl, valnou hromadu obvyklým křestanskýra pozdravem, a poděkovav Jeho Eminencí vroucné za čest, kterou Svou přítomností celému shromáždění prokázal, představil zástupce diecésí a sice vdpp. kanovníky: Thlg. Dra. Michla za Litoměřicko, Frant. Dichtla za Budějovicko, a Thlg. Dra. Frant. X. Kryštůfka, prorektora a c. k. profesora české university Karlo Ferdinandské za Králohradecko. — Po té sdělil pan předseda obsah telegramu z Říma, dle něhož jest Jeho Eminencí splnomocněn, uclěliti přítomným apoštolské požehnání ; telegram zní : .Papež Lev XIII. uděluje Vaší Eminencí plnou moc, uděliti apoštolské požehnání bratrstvu sv. Michaela, jakož i všem při slavné schůzi přítomným.“";
        assertEquals(expected, actual);
    }
}
