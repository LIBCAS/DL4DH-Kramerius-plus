package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper;

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
        private String model;
    }
}
