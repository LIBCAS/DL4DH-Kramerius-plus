package dl4dh;

import cz.inqool.dl4dh.persistence.PersistenceApplicationContext;
import cz.inqool.dl4dh.persistence.dao.MonographRepository;
import cz.inqool.dl4dh.persistence.dao.PageRepository;
import cz.inqool.dl4dh.persistence.entity.Monograph;
import cz.inqool.dl4dh.persistence.entity.Page;
import cz.inqool.dl4dh.persistence.entity.Token;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Norbert Bodnar
 */
@SpringBootTest(classes = PersistenceApplicationContext.class)
public class MonographRepositoryTest {

    private Monograph monograph;

    @Autowired
    private MonographRepository monographRepository;

    @Autowired
    private PageRepository pageRepository;

    @BeforeEach
    public void setup() {
        List<Token> tokens = new ArrayList<>();

        Token token = new Token();
        token.setContent("Hello");
        token.setTokenIndex(0);
        token.setStartOffset(0);
        token.setEndOffset(5);
        tokens.add(token);

        token = new Token();
        token.setContent("world");
        token.setTokenIndex(1);
        token.setStartOffset(6);
        token.setEndOffset(11);
        tokens.add(token);

        token = new Token();
        token.setContent("!");
        token.setTokenIndex(2);
        token.setStartOffset(12);
        token.setEndOffset(13);
        tokens.add(token);

        List<Page> pages = new ArrayList<>();
        Page page = new Page();
        page.setPid("uuid:1_1");
        page.setTokens(tokens);
        page.setPageNumber("[1a]");
        page.setRootId("uuid:1");
        page.setPageIndex(0);
        pages.add(page);

        monograph = new Monograph();
        monograph.setPid("uuid:1");
        monograph.setTitle("Test monograph");
        monograph.setPolicy("Public");
        monograph.setPages(pages);
    }

    @Test
    public void testSave() {
        pageRepository.saveAll(monograph.getPages());
        monographRepository.save(monograph);

        List<Monograph> monographs = monographRepository.findAll();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(monographs.size()).isEqualTo(1);

            Monograph storedMonograph = monographs.get(0);
            softAssertions.assertThat(storedMonograph.getPages().size()).isEqualTo(0);
            softAssertions.assertThat(storedMonograph).isEqualTo(monograph);
        });
    }

    @Test
    public void testFindAllByRootId() {
        String rootId = "uuid:1c1799a1-53a1-4962-8357-d174c19e6fc9";
        List<Page> pages = createPagesForRootId(rootId, 100);
        pageRepository.saveAll(pages);

        List<Page> pagesForMonograph = pageRepository.findAllByRootIdOrderByPageIndexAsc(rootId);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(pagesForMonograph.size()).isEqualTo(100);

            int counter = 0;
            for (Page page : pagesForMonograph) {
                softAssertions.assertThat(page.getTokens().size()).isEqualTo(4);
                softAssertions.assertThat(page.getPageNumber()).isEqualTo("[" + counter + "]");
                softAssertions.assertThat(page.getPid()).isEqualTo(rootId + "_" + counter);

                counter++;
            }
        });
    }

    @Test
    public void testFindPageableByRootId() {
        String rootId = "uuid:96a2a6ed-84f8-41e0-813d-163332102212";
        List<Page> pages = createPagesForRootId(rootId, 200);
        pageRepository.saveAll(pages);

        List<Page> firstTenPages = pageRepository.findByRootIdOrderByPageIndexAsc(rootId, PageRequest.of(0,10));

        List<Page> pages150To180 = pageRepository.findByRootIdOrderByPageIndexAsc(rootId, PageRequest.of(5, 30));

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(firstTenPages.size()).isEqualTo(10);
            softAssertions.assertThat(pages150To180.size()).isEqualTo(30);

            int counter = 0;
            for (Page page : firstTenPages) {
                softAssertions.assertThat(page.getTokens().size()).isEqualTo(4);
                softAssertions.assertThat(page.getPageNumber()).isEqualTo("[" + counter + "]");
                softAssertions.assertThat(page.getPid()).isEqualTo(rootId + "_" + counter);

                counter++;
            }

            counter = 150;
            for (Page page : pages150To180) {
                softAssertions.assertThat(page.getTokens().size()).isEqualTo(4);
                softAssertions.assertThat(page.getPageNumber()).isEqualTo("[" + counter + "]");
                softAssertions.assertThat(page.getPid()).isEqualTo(rootId + "_" + counter);

                counter++;
            }
        });
    }

    private List<Page> createPagesForRootId(String rootId, int numberOfPages) {
        List<Page> pages = new ArrayList<>();
        Page page;
        List<Token> tokens;
        Token token;
        // create 100 pages
        for (int i = 0; i < numberOfPages; i++) {
            tokens = new ArrayList<>();

            token = new Token();
            token.setContent("Hello");
            token.setTokenIndex(0);
            token.setStartOffset(0);
            token.setEndOffset(5);
            tokens.add(token);

            token = new Token();
            token.setContent("world");
            token.setTokenIndex(1);
            token.setStartOffset(6);
            token.setEndOffset(11);
            tokens.add(token);

            token = new Token();
            token.setContent("-");
            token.setTokenIndex(2);
            token.setStartOffset(12);
            token.setEndOffset(13);
            tokens.add(token);

            token = new Token();
            token.setContent(String.valueOf(i));
            token.setTokenIndex(3);
            token.setStartOffset(14);
            token.setEndOffset(15);
            tokens.add(token);

            page = new Page();
            page.setPid(rootId + "_" + i);
            page.setTokens(tokens);
            page.setPageNumber("[" + i + "]");
            page.setRootId(rootId);
            page.setPageIndex(i);
            pages.add(page);
        }

        return pages;
    }

}
