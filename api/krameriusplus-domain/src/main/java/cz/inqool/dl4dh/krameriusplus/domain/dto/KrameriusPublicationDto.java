package cz.inqool.dl4dh.krameriusplus.domain.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.inqool.dl4dh.krameriusplus.domain.enums.PublicationModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "model")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = KrameriusMonographDto.class, name = "monograph")
})
public abstract class KrameriusPublicationDto {

    protected String pid;

    protected String title;

    protected List<String> collections;

    protected String policy;

    public abstract PublicationModel getModel();
}
