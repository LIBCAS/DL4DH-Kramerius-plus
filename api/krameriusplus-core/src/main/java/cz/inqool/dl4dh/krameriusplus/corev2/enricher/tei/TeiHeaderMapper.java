package cz.inqool.dl4dh.krameriusplus.corev2.enricher.tei;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsName;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsNamePart;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsOriginInfo;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface TeiHeaderMapper {

    @Mappings({
            @Mapping(target = "author", source = "modsMetadata.name"),
            @Mapping(target = "physicalDescription", source = "modsMetadata.physicalDescription"),
            @Mapping(target = "originInfo", source = "modsMetadata.originInfo"),
            @Mapping(target = "identifiers", source = "modsMetadata.identifiers")
    })
    TeiHeaderInput map(Publication publication);


    default TeiHeaderInput.TeiAuthor map(ModsName modsName) {
        if (modsName == null) {
            return null;
        }

        TeiHeaderInput.TeiAuthor teiAuthor = new TeiHeaderInput.TeiAuthor();
        teiAuthor.setType(modsName.getType());
        teiAuthor.setName(modsName.getNameParts().stream().map(ModsNamePart::getValue).collect(Collectors.joining(",")));
        teiAuthor.setIdentifier(modsName.getNameIdentifier());

        return teiAuthor;
    }

    default TeiHeaderInput.TeiOriginInfo map(List<ModsOriginInfo> originInfo) {
        if (originInfo == null || originInfo.isEmpty()) {
            return null;
        }

        ModsOriginInfo firstOriginInfo = originInfo.get(0);

        TeiHeaderInput.TeiOriginInfo teiOriginInfo = new TeiHeaderInput.TeiOriginInfo();
        teiOriginInfo.setPublisher(String.join(",", firstOriginInfo.getPublishers()));

        if (firstOriginInfo.getDateIssued() != null) {
            teiOriginInfo.setDate(firstOriginInfo.getDateIssued().getValue());
        }

        if (firstOriginInfo.getPlaces() != null && !firstOriginInfo.getPlaces().isEmpty()) {
            List<String> teiPlaces = new ArrayList<>();
            for (var place : firstOriginInfo.getPlaces()) {
                if ("text".equals(place.getType())) {
                    teiPlaces.add(place.getValue());
                }
            }

            teiOriginInfo.setPlaces(teiPlaces);
        }

        return teiOriginInfo;
    }
}
