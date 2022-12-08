package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ChainCreateWrapper {

    private final Map<String, String> publicationIdToTitle = new HashMap<>();

    private String enrichmentItemId;
}
