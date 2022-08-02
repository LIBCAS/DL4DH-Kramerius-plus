package cz.inqool.dl4dh.krameriusplus.core.domain;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter.EqFilter;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.PageStore;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.monograph.MonographUnit;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.periodical.Periodical;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@AutoConfigureDataMongo
public class DomainStoreTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    private PublicationStore publicationStore;

    private PageStore pageStore;

    @BeforeEach
    @Autowired
    void setUp(PageStore pageStore, PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
        this.pageStore = pageStore;
    }

    @AfterEach
    void cleanUp() {
        mongoTemplate.getDb().drop();
    }

    @Test
    public void storeOne() {
        Publication monograph = new Monograph();

        publicationStore.create(monograph);

        List<Publication> publications = publicationStore.listAll();
        assertEquals(1, publications.size());
        assertEquals(monograph, publications.get(0));
    }

    @Test
    public void storeMultipleSameType() {
        List<Publication> publications = new ArrayList<>();
        publications.add(new Monograph());
        publications.add(new Monograph());

        publicationStore.create(publications);

        List<Publication> actual = publicationStore.listAll();

        assertEquals(2, actual.size());
    }

    @Test
    public void storeMultipleDifferentType() {
        List<Publication> publications = new ArrayList<>();
        publications.add(new Monograph());
        publications.add(new Periodical());
        publications.add(new MonographUnit());

        publicationStore.create(publications);

        List<Publication> actual = publicationStore.listAll();

        assertEquals(3, actual.size());
        assertTrue(actual.stream().anyMatch(pub -> pub instanceof Monograph));
        assertTrue(actual.stream().anyMatch(pub -> pub instanceof Periodical));
        assertTrue(actual.stream().anyMatch(pub -> pub instanceof MonographUnit));
    }

    @Test
    public void find() {
        Publication publication = new Monograph();
        publication.setId("1");
        publication.setTitle("Title 1");
        publicationStore.create(publication);

        Publication actual = publicationStore.find("1");

        Assertions.assertEquals(publication.getId(), actual.getId());
        assertEquals(publication.getTitle(), actual.getTitle());
    }

    @Test
    public void findWithCustomFields() {
        Publication publication = new Monograph();
        publication.setId("1");
        publication.setTitle("Title 1");
        publication.setPolicy("Policy 1");
        publicationStore.create(publication);

        List<String> includeFields = new ArrayList<>();
        includeFields.add("_id");
        includeFields.add("title");

        Publication actual = publicationStore.find("1", includeFields);

        Assertions.assertEquals(publication.getId(), actual.getId());
        assertEquals(publication.getTitle(), actual.getTitle());
        assertNull(actual.getPolicy());
    }

    @Test
    public void update() {
        Publication expected = new Monograph();
        expected.setId("1");
        expected.setTitle("title");

        publicationStore.create(expected);

        expected = publicationStore.find("1");
        expected.setTitle("new title");

        publicationStore.update(expected);

        Publication actual = publicationStore.find("1");

        assertEquals("new title", actual.getTitle());
    }

    @Test
    public void listByIds() {
        for (int i = 0; i < 10; i++) {
            Publication publication = new MonographUnit();
            publication.setId(String.valueOf(i));
            publicationStore.create(publication);
        }

        Collection<Publication> actual = publicationStore.list(
                IntStream.range(0, 10)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.toList()));

        assertEquals(10, actual.size());
        assertTrue(IntStream.range(0, 10)
                .mapToObj(String::valueOf)
                .allMatch(id -> actual.stream().anyMatch(pub -> pub.getId().equals(id))));
    }

    @Test
    public void findWithPages() {
        List<Page> pages = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            Page page = new Page();
            page.setId(String.valueOf(j));
            page.setContent("Content of the page " + j);
            pages.add(pageStore.create(page));
        }

        Publication publication = new Monograph();
        publication.setId("1");
        publication.setTitle("Title");
        publication.setPolicy("Policy");
        publication.setPages(pages);
        publicationStore.create(publication);

        Publication actual = publicationStore.find("1", getIncludeFields("title", "pages"));

        assertEquals(10, actual.getPages().size());
        Assertions.assertEquals("1", actual.getId());
        assertEquals("Title", actual.getTitle());
        assertNull(actual.getPolicy());
    }

    @Test
    public void findWithoutPages() {
        List<Page> pages = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            Page page = new Page();
            page.setId(String.valueOf(j));
            page.setContent("Content of the page " + j);
            pages.add(pageStore.create(page));
        }

        Publication publication = new Monograph();
        publication.setId("1");
        publication.setTitle("Title");
        publication.setPolicy("Policy");
        publication.setPages(pages);
        publicationStore.create(publication);

        Publication actual = publicationStore.find("1", getIncludeFields("title", "policy"));

        assertEquals(0, actual.getPages().size());
        Assertions.assertEquals("1", actual.getId());
        assertEquals("Title", actual.getTitle());
        assertEquals("Policy", actual.getPolicy());
    }

    @Test
    public void findByParams() {
        Params params = new Params();
        params.includeFields("title", "pages");
        params.addFilters(new EqFilter("title", "Title1"));

        for (int i = 0; i < 10; i++) {
            List<Page> pages = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                Page page = new Page();
                page.setId(String.valueOf(i * 10 + j));
                page.setContent("Content of the page " + i * 10 + j);
                pages.add(pageStore.create(page));
            }

            Publication publication = new MonographUnit();
            publication.setId(String.valueOf(i));
            publication.setTitle("Title" + i);
            publication.setPolicy("Policy" + i);
            publication.setPages(pages);
            publicationStore.create(publication);
        }

        List<Publication> actual = publicationStore.listAll(params);

        assertEquals(1, actual.size());
        assertEquals(10, actual.get(0).getPages().size());
        assertEquals("Title1", actual.get(0).getTitle());
    }

    @Test
    public void deleteOne() {
        List<Page> pages = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            Page page = new Page();
            page.setId(String.valueOf(j));
            page.setContent("Content of the page " + j);
            pages.add(pageStore.create(page));
        }

        Publication publication = new MonographUnit();
        publication.setId("1");
        publication.setTitle("Title");
        publication.setPolicy("Policy");
        publication.setPages(pages);

        publicationStore.create(publication);

        boolean deleted = publicationStore.delete(publication);

        assertTrue(deleted);
    }

    @Test
    public void deleteMultiple() {
        List<Publication> publications = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            List<Page> pages = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                Page page = new Page();
                page.setId(String.valueOf(i * 10 + j));
                page.setContent("Content of the page " + i * 10 + j);
                pages.add(pageStore.create(page));
            }

            Publication publication = new MonographUnit();
            publication.setId(String.valueOf(i));
            publication.setTitle("Title" + i);
            publication.setPolicy("Policy" + i);
            publication.setPages(pages);
            publications.add(publicationStore.create(publication));
        }

        long deletedCount = publicationStore.delete(publications);

        assertEquals(10, deletedCount);
    }

    private List<String> getIncludeFields(String... fields) {
        return new ArrayList<>(Arrays.asList(fields));
    }
}
