package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.api.publication.DigitalObjectContext;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.monograph.Pdf;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class PublicationCreateDto extends DigitalObjectCreateDto {

    private List<List<DigitalObjectContext>> context = new ArrayList<>();

    private List<String> collections = new ArrayList<>();

    private HandleCreateDto handle;

    @JsonProperty("dnnt-labels")
    private List<String> dnntLabels = new ArrayList<>();

    private Boolean dnnt;

    private Pdf pdf;
}
