package cz.inqool.dl4dh.krameriusplus.api.rest;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.PageStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestApi {

    private final PageStore pageStore;

    @Autowired
    public TestApi(PageStore pageStore) {
        this.pageStore = pageStore;
    }

    @GetMapping
    public void test() {
        Page page = new Page();

        pageStore.save(page);
    }
}
