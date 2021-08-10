package cz.inqool.dl4dh.krameriusplus.service;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.EnricherApplicationContext;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.streams.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.utils.AltoUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = EnricherApplicationContext.class)
public class AltoUtilsTest {

    private AltoUtils altoUtils;

    @Autowired
    private StreamProvider streamProvider;

    @Test
    public void testExtractStringFromAlto() {
        String pageId = "uuid:5029b5a1-32f3-4fa5-a747-5483cc56426f";

        Alto alto = streamProvider.getAlto(pageId);

        String pageContentString = AltoUtils.extractPageContent(alto);

        String expected = "— 4 — Vdp. předseda vzpoměl úcty, jaké těšil se zesnulý arcipastýř náš, kardinál Schwarzenberg u Jeho Svatosti papeže Lva XIII., jemuž prsten rybářský, odznak apoštolského poslání byl odevzdal, a podotkl, že lásku svou přenesl Lev XHL i na nynějšího arcipastýře našeho a naší milou českou vlast, pro níž v duchu svornosti vzděláváni jsou v české koleji římské, kterouž svaty Otec tak štědře podporuje jinoši z království Českého, na vzorné kněze a syny církve. Po srdečném proslovu vdp. předsedy zapělo shromáždění nadšeně a s povznášející vroucností papežskou hymnu. Předseda: Prosím slovutného pana doktora Trakala, aby svou přednášku započal. slovutného pána JUDr. JOSEFA TRAKALA, riditele Strakovské akademie. Vaše Eminencii Slavné shromáždění! í , •. • 't . ■ • . . . t Letošní rok přinesl nám dvojí v dějinách řídká jubilea. Spolu s 50. ročnicí panování našeho vznešeného císaře a krále slavíme významné jubileum církevní světového, všeuárodního, všelidského rázu. Řízením božím v plné svěžesti ducha i těla dokonává sv. Otec letos 601eté kněžství (diamantové jubileum), zároveň pak 551eté biskupství, 451etý kardinalát a 201etý pontifikát, jehož neumorným působením, posvěceným nejvyšším statkům lidstva ■ dle svědectví přátel i odpůrců sv. Stolice, rozproudil se ve všech žilách staré církve život nový, opravňující к nadějím neobyčejného nastávajícího rozkvětu věci katolické. К posouzení velikých výsledků nynějšího papežství není nein ístno pohlédnouti aspoň letem na běh života sv. Otce, spadající téměř v jedno s celým stoletím 19. Rodina hrabat Pecci, která dala církvi nynějšího, arcipastýřei usedlá jest od 16. století na zemanském.statku v městečku Carpineto v hornatině volské mezi Římem a Neapoli. Zde narodil se 2. března 1810 hrabě J o achira Vincenc Pecci, zde v zbožných tradicích rodiny církvi a sv. Stoliči věrně oddané dostalo se mu první pečlivé výchovy. Klasi.ckého vzdělání nabyl společné s bratrem svým Josefem — později, jak známo, kardinálem sv. církve — v koleji Jesuitů ve Viterbu, К";
        assertEquals(expected, pageContentString);
    }
}
