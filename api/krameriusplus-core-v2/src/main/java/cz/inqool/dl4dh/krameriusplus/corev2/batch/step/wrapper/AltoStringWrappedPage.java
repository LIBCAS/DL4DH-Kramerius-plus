package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AltoStringWrappedPage {

    private final Page page;

    private final String alto;

}
