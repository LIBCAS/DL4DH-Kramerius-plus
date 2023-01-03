package cz.inqool.dl4dh.krameriusplus.core.batch.step.wrapper;

import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DigitalObjectExport {

    private final DigitalObject digitalObject;

    private final String content;

    private final String filename;

}
