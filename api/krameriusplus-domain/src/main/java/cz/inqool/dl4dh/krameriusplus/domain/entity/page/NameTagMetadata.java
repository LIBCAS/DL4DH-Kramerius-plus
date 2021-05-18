package cz.inqool.dl4dh.krameriusplus.domain.entity.page;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.NamedEntity;
import cz.inqool.dl4dh.krameriusplus.domain.enums.NamedEntityType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class NameTagMetadata {

    private Map<NamedEntityType, List<NamedEntity>> namedEntities = new TreeMap<>();

    public void add(NamedEntity namedEntity) {
        NamedEntityType namedEntityType = NamedEntityType.fromString(namedEntity.getEntityType());

        namedEntities.putIfAbsent(namedEntityType, new ArrayList<>());
        namedEntities.get(namedEntityType).add(namedEntity);
    }
}
