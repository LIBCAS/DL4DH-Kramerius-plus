package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.DigitalObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DigitalObjectExport {

    private final DigitalObject digitalObject;

    private final String content;

    private final String filename;

}
