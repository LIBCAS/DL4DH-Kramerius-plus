package cz.inqool.dl4dh.krameriusplus.api.publication.page;

import com.fasterxml.jackson.annotation.JsonTypeName;
import cz.inqool.dl4dh.krameriusplus.api.publication.object.DigitalObjectDto;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMetadata;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.nametag.NameTagMetadataDto;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.udpipe.TokenDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.PublicationModelName.PAGE;


@Getter
@Setter
@JsonTypeName(PAGE)
public class PageDto extends DigitalObjectDto {

    private List<TokenDto> tokens = new ArrayList<>();

    private String policy;

    private String pageType;

    /**
     * Storing it as a string for page numbers like "[1a]"
     */
    private String pageNumber;

    private NameTagMetadataDto nameTagMetadata;

    private MetsMetadata metsMetadata;
}
