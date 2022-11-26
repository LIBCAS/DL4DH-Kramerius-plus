package cz.inqool.dl4dh.krameriusplus.api.publication.page.nametag;

import cz.inqool.dl4dh.krameriusplus.api.publication.page.udpipe.TokenDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class NamedEntityDto {

    private List<TokenDto> tokens = new ArrayList<>();

    private String entityType;
}
