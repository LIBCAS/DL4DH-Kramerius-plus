package cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.tei.header;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.ModsMetadata.Identifier;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.ModsMetadata.PhysicalDescription;
import lombok.Setter;

import java.util.List;

@Setter
public class TeiHeaderInput {

    private String title;

    private TeiAuthor author;

    private PhysicalDescription physicalDescription;

    private TeiOriginInfo originInfo;

    private List<Identifier> identifiers;

    @Setter
    public static class TeiAuthor {
        private String type;
        private String name;
        private String identifier;
    }

    @Setter
    public static class TeiOriginInfo
    {
        private String publisher;
        private String date;
        private List<String> places;
    }
}
