package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.file.Path;

@Getter
@AllArgsConstructor
public class NdkFilePathWrappedPage {

    private final Page page;

    private final Path ndkFilePath;
}
