package cz.inqool.dl4dh.krameriusplus.api.publication.page.nametag;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class NameTagMetadataDto {

    private Map<NamedEntityType, List<NamedEntityDto>> namedEntities = new TreeMap<>();
}
