package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChainCreateDto {

    private final List<String> publicationIds = new ArrayList<>();

    private String enrichmentItemId;
}
