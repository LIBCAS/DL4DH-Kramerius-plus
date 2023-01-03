package cz.inqool.dl4dh.krameriusplus.core.enricher.tei;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsIdentifier;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsPhysicalDescription;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TeiHeaderInput {

    private String title;

    private TeiAuthor author;

    private ModsPhysicalDescription physicalDescription;

    private TeiOriginInfo originInfo;

    private List<ModsIdentifier> identifiers;

    @Setter
    public static class TeiAuthor {
        private String type;
        private String name;
        private String identifier;
    }

    @Setter
    public static class TeiOriginInfo {
        private String publisher;
        private String date;
        private List<String> places;
    }
}
