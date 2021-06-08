package cz.inqool.dl4dh.krameriusplus.service;

import cz.inqool.dl4dh.krameriusplus.EnricherApplicationContext;
import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.MonographWithPages;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.LinguisticMetadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Token;
import cz.inqool.dl4dh.krameriusplus.service.enricher.UDPipeService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Norbert Bodnar
 */
@SpringBootTest(classes = EnricherApplicationContext.class)
public class UDPipeServiceTest {

    @Autowired
    private UDPipeService tokenizer;

    @Test
    public void testTokenize() {
        String content = "Pojedeme k babičce, abychom se najedli.";

        List<Token> actual = tokenizer.tokenize(content);
        List<Token> expected = getExpectedResult();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(8);
            softAssertions.assertThat(actual).isEqualTo(expected);
        });
    }

    @Test
    public void testBulk() {
        MonographWithPages monograph = prepareMonographDto();

        List<Page> pages = monograph.getPages();

        assertEquals(3, pages.size());

        assertEquals(336, pages.get(0).getTokens().size());
        assertEquals(335, pages.get(0).getTokens().get(335).getTokenIndex());

        assertEquals(306, pages.get(1).getTokens().size());
        assertEquals(305, pages.get(1).getTokens().get(305).getTokenIndex());

        assertEquals(346, pages.get(2).getTokens().size());
        assertEquals(345, pages.get(2).getTokens().get(345).getTokenIndex());
    }

    private MonographWithPages prepareMonographDto() {
        MonographWithPages monograph = new MonographWithPages();
        monograph.setId("uuid:9a9690e0-69e8-11eb-9f97-005056827e51");
        monograph.setTitle("Pan učitel");
        monograph.setPages(new ArrayList<>());

        Page page = new Page();
        page.setId("uuid:2f28ca1f-e874-4cd0-83d4-3aae9a667449");
        page.setPageNumber("4");
        page.setTokens(tokenizer.tokenize(("í ikat od rodičů i od žáků, a v té důminoe, že se\n" +
                "nesníží, jak toho třeba, k žákům svým ani v mlu-\n" +
                "vení, ani v obcování; pracujeť obyčejně napřed\n" +
                "se zámožnějšími, a poněvadž jak desátá a čtvrtá\n" +
                "hodina padne, od učení přestává a pro děti nuz-\n" +
                "nějších rodičů času mu nezbývá, v hodince, v při-\n" +
                "váté, za něž zvláště má placeno, odhaluje teprv\n" +
                "tajnosti všelikerá umění,, dělá nejdůležitější ú-\n" +
                "lohy, opravuje bedlivěji a mírněji všeliké pokles-\n" +
                "ky dětské. Však nižší městské školy bývají velmi\n" +
                "přeplněné a těžko jimi к vyšším, prázdnějším\n" +
                "třídám probřednouti.\n" +
                "Znala jsem školu jen dle pověsti, co mně o ní\n" +
                "sousedovic děti vypravovaly, a to takové, že jsem\n" +
                "měla před školou strach. К tomu i domácí při-\n" +
                "spívali; neboť jakmile jsem něco dokázala, hned\n" +
                "mi vyhrožovali: „No počkej, počkej, jen až při-\n" +
                "jdeš do školy, však oni tě tam naučí být tichou!“\n" +
                "A stará chůva, chtíc mne potěšit, říkala: „Milá\n" +
                "dušinko, to už jinak na světě není, učení je mu-\n" +
                "čení, to si musíme každý přetrpět. Já když cho-\n" +
                "dila do školy, byla jsem bita jako žito.“ Slýchala\n" +
                "jsem též poklasnou, ana se proti škole zpouzí-\n" +
                "vala a vzdorně tvrdívala, že své dítě do školy ne-\n" +
                "pošle; že sama do školy nechodila, že ač neumí\n" +
                "číst ani psát, přec živa; aby její dítě umělo na\n" +
                "modlitbách říkat a se podepsat, tomu že je tatík\n" +
                "sám za několik zimních večerů naučí. Viděla\n" +
                "jsem tutéž poklasnou, anaž jindy sama své dítě\n" +
                "nemilosrdně prala, a křikem a láním ke škole\n" +
                "vyprovázela,, byvši к tomu úředně donucena. Ne-\n" +
                "bylo tedy divu, že jsem se školy bála co nějaké\n" +
                "mučírny.\n" +
                "Když jsem první ráno v Chvalíně se probudila,\n" +
                "zdálo se mi, že nikomu není tak zle na světě,\n" +
                "4").replaceAll("-\n", "").replaceAll("\n", " ")));
        monograph.getPages().add(page);

        page = new Page();
        page.setId("uuid:e97daf16-c92f-4548-937a-47e3cdc3278d");
        page.setPageNumber("9");
        page.setTokens(tokenizer.tokenize(("kleknul před křížem a předříkával nám krátké\n" +
                "sice, ale dojemné modlitby. Po modlitbě násle-\n" +
                "dovala píseň obsahu nábožného, aneb podněcu-\n" +
                "jící k učení, к mravnosti, к pilnosti, načež jsme\n" +
                "začali se učit.\n" +
                "Ohlížela jsem se chvílemi po rákosce, po os-\n" +
                "lovské lavici, po pytlíčku s hrachem, na němž\n" +
                "prý děvčata klečívají, po černé tabulce a po ji-\n" +
                "ných strašidlech, jež jsem si z domova přinesla.\n" +
                "Ale ve chvalínské škole nebylo ani oslpvské la-\n" +
                "vice, ani rákosky, ani černé, ani jiné potupné\n" +
                "tabulky. V čele světnice, na bílé stěně, visel pěk-\n" +
                "ný obraz: Kristus, jak žehná maličkým; nad stol-\n" +
                "kem pana učitele visela podobizna císaře pána\n" +
                "a vyobrazení města Prahy a na poboční zdi vi-\n" +
                "sela mapa Cech a v rámci za sklem čistě na-\n" +
                "psaná mravná průpověď, obyčejně každý týden\n" +
                "jiná. V pondělí totiž vyvěsil pan učitel při kraso-\n" +
                "psaní předpis ten, který v celém předešlém tém-\n" +
                "dni byl nejpěknější. Psali jsme při krasopisu\n" +
                "vždy toliko jednu průpověď. Napřed ukázal pan\n" +
                "učitel jednotlivá, buď těžší aneb snad méně zná-\n" +
                "má písmena, kteréžto jsme po něm psali na\n" +
                "tabulkách a v sešitech svých; též slova celá na-\n" +
                "psal zprvu, a prohlédnuv hnedle všem písařům\n" +
                "práce jejich, shledal, že to půjde a napsal ko-\n" +
                "nečně celou, avšak jen jednu a krátkou průpo-\n" +
                "věď. Bývaliť průpovědi ty vždy užitečné, a poně-\n" +
                "vadž byly krátké, a my je po celou hodinu psali\n" +
                "—\tpři čemž nám neustále pan učitel po ruce byl\n" +
                "—\thluboce v paměti nám utkvěly. Kdo tedy tu\n" +
                "neb onu průpověď nejpěkněji napsal, tomu sta-\n" +
                "la se čest, že dál pan učitel písmo jeho za sklo,\n" +
                "kde zůstalo až zase do druhého pondělí.\n" +
                "Nedaleko dveří v koutě byla zelená kachlová,\n" +
                "9").replaceAll("-\n", "").replaceAll("\n", " ")));
        monograph.getPages().add(page);

        page = new Page();
        page.setId("uuid:2765ffab-b9d3-4f8a-a795-b8a0d68b9558");
        page.setPageNumber("12");
        page.setTokens(tokenizer.tokenize(("učil malé děti ve zvláštní světnici. Já seděla v tře-\n" +
                "tí lavici, neboť jsem již uměla dosti obstojně číst,\n" +
                "začínala jsem psát, a počítat jsem znala do dva-\n" +
                "cíti, když jsem do školy přišla. Ačkoli jsem hned\n" +
                "na začátku mezi čtenáře přišla, přece s pana u-\n" +
                "čitele jsem nespustila ani zraku ani sluchu, když\n" +
                "učil on číst. Byl mi způsob jeho zcela nový.\n" +
                "Napsal totiž hlásky, na příklad „m“, a napodobil\n" +
                "mumlání medvědovo a vypravoval o něm, což\n" +
                "i děti dělaly; pak к té mumlavé souhlásce při-\n" +
                "psal buď „a“, buď „e“, buď „i\", aneb „o“, a děti\n" +
                "hned četly „ma“, „me“ bez slabikování, píšíce\n" +
                "spolu na svých tabulkách. Měl také písmenka\n" +
                "na tabulkách nalepená, která děti vyhledávaly\n" +
                "a do složek od něho vyřknutých skládaly. Jaká\n" +
                "to bývala radost, jaký řehtot, když takto na ta-\n" +
                "bulce byla „máma“ aneb „táta“, aneb „kotě“;\n" +
                "neb „jahoda“, aneb něco jiného, co děti zají-\n" +
                "malo! S jakou radostí, jakým smíchem si to kaž-\n" +
                "dé to pouzdrátko na svou tabulku křídou aneb\n" +
                "rafijí psalo! To mi bylo zcela nové, a já upřímně\n" +
                "spolu se smála a v radostných posunkách pře-\n" +
                "kvapených maličkých sebe sama viděla, ač jsem\n" +
                "se tomu tak neučila. Neboť když mi byly čtyry\n" +
                "léta, přinesla mi matka s jarmarku tabulku, na\n" +
                "níž byl lístek potištěn černými literkami; nad\n" +
                "nimi byl vymalovaný červený kohoutek. „Tu máš\n" +
                "tabulku, když si pamatuješ písničky, můžeš si\n" +
                "pamatovat i písmenka,“ pravila mi matka.\n" +
                "V sousedství bydlel pan strýček, tak ho všickni\n" +
                "volali, ačkoliv nebyl nikoho strýcem ; také o něm\n" +
                "povídali, že umí víc než chleba jíst. К tomu jsem\n" +
                "druhý den šla tabulkou se pochlubit, a on sám\n" +
                "se nabídnul, že mne naučí literky znát. I naučil\n" +
                "mne je znát, potom dohromady skládat v sla-\n" +
                "12").replaceAll("-\n", "").replaceAll("\n", " ")));
        monograph.getPages().add(page);

        return monograph;
    }

    private List<Token> getExpectedResult() {
        List<Token> expected = new ArrayList<>();

        Token token = new Token();
        token.setContent("Pojedeme");
        token.setTokenIndex(0);
        token.setStartOffset(0);
        token.setEndOffset(8);
        LinguisticMetadata metadata = new LinguisticMetadata();
        metadata.setPosition(1);
        metadata.setLemma("jet");
        metadata.setUPosTag("VERB");
        metadata.setXPosTag("VB-P---1F-AA---");
        metadata.setFeats("Mood=Ind|Number=Plur|Person=1|Polarity=Pos|Tense=Fut|VerbForm=Fin|Voice=Act");
        metadata.setHead("0");
        metadata.setDepRel("root");
        metadata.setMisc("TokenRange=0:8");
        token.setLinguisticMetadata(metadata);
        expected.add(token);

        token = new Token();
        token.setContent("k");
        token.setTokenIndex(1);
        token.setStartOffset(9);
        token.setEndOffset(10);
        metadata = new LinguisticMetadata();
        metadata.setPosition(2);
        metadata.setLemma("k");
        metadata.setUPosTag("ADP");
        metadata.setXPosTag("RR--3----------");
        metadata.setFeats("AdpType=Prep|Case=Dat");
        metadata.setHead("3");
        metadata.setDepRel("case");
        metadata.setMisc("TokenRange=9:10");
        token.setLinguisticMetadata(metadata);
        expected.add(token);

        token = new Token();
        token.setContent("babičce");
        token.setTokenIndex(2);
        token.setStartOffset(11);
        token.setEndOffset(18);
        metadata = new LinguisticMetadata();
        metadata.setPosition(3);
        metadata.setLemma("babička");
        metadata.setUPosTag("NOUN");
        metadata.setXPosTag("NNFS3-----A----");
        metadata.setFeats("Case=Dat|Gender=Fem|Number=Sing|Polarity=Pos");
        metadata.setHead("1");
        metadata.setDepRel("obl");
        metadata.setMisc("SpaceAfter=No|TokenRange=11:18");
        token.setLinguisticMetadata(metadata);
        expected.add(token);

        token = new Token();
        token.setContent(",");
        token.setTokenIndex(3);
        token.setStartOffset(18);
        token.setEndOffset(19);
        metadata = new LinguisticMetadata();
        metadata.setPosition(4);
        metadata.setLemma(",");
        metadata.setUPosTag("PUNCT");
        metadata.setXPosTag("Z:-------------");
        metadata.setHead("8");
        metadata.setDepRel("punct");
        metadata.setMisc("TokenRange=18:19");
        token.setLinguisticMetadata(metadata);
        expected.add(token);

        //TODO: word "abychom" is tokenized into two tokens -> might change the way it is stored in the future
        token = new Token();
        token.setContent("abychom");
        token.setTokenIndex(4);
        token.setStartOffset(20);
        token.setEndOffset(27);
        metadata = new LinguisticMetadata();
        metadata.setPosition(5);
        metadata.setLemma("aby");
        metadata.setUPosTag("SCONJ");
        metadata.setXPosTag("J,-------------");
        metadata.setHead("8");
        metadata.setDepRel("mark");
        metadata.setMisc("TokenRange=20:27");
        token.setLinguisticMetadata(metadata);
        expected.add(token);

        token = new Token();
        token.setContent("se");
        token.setTokenIndex(5);
        token.setStartOffset(28);
        token.setEndOffset(30);
        metadata = new LinguisticMetadata();
        metadata.setPosition(7);
        metadata.setLemma("se");
        metadata.setUPosTag("PRON");
        metadata.setXPosTag("P7-X4----------");
        metadata.setFeats("Case=Acc|PronType=Prs|Reflex=Yes|Variant=Short");
        metadata.setHead("8");
        metadata.setDepRel("expl:pv");
        metadata.setMisc("TokenRange=28:30");
        token.setLinguisticMetadata(metadata);
        expected.add(token);

        token = new Token();
        token.setContent("najedli");
        token.setTokenIndex(6);
        token.setStartOffset(31);
        token.setEndOffset(38);
        metadata = new LinguisticMetadata();
        metadata.setPosition(8);
        metadata.setLemma("najíst");
        metadata.setUPosTag("VERB");
        metadata.setXPosTag("VpMP---XR-AA---");
        metadata.setFeats("Animacy=Anim|Gender=Masc|Number=Plur|Polarity=Pos|Tense=Past|VerbForm=Part|Voice=Act");
        metadata.setHead("1");
        metadata.setDepRel("advcl");
        metadata.setMisc("SpaceAfter=No|TokenRange=31:38");
        token.setLinguisticMetadata(metadata);
        expected.add(token);

        token = new Token();
        token.setContent(".");
        token.setTokenIndex(7);
        token.setStartOffset(38);
        token.setEndOffset(39);
        metadata = new LinguisticMetadata();
        metadata.setPosition(9);
        metadata.setLemma(".");
        metadata.setUPosTag("PUNCT");
        metadata.setXPosTag("Z:-------------");
        metadata.setHead("1");
        metadata.setDepRel("punct");
        metadata.setMisc("SpaceAfter=No|TokenRange=38:39");
        token.setLinguisticMetadata(metadata);
        expected.add(token);

        return expected;
    }
}
