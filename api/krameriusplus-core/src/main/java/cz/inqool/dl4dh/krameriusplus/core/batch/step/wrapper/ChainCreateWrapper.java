package cz.inqool.dl4dh.krameriusplus.core.batch.step.wrapper;

import cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChainCreateWrapper {

    private List<PublicationData> publications = new ArrayList<>();

    private String enrichmentItemId;

    @Getter
    @Setter
    public static class PublicationData {
        private String publicationId;
        private String publicationTitle;
        private KrameriusModel model;
    }
}
