package cz.inqool.dl4dh.krameriusplus.dto.tei;

import cz.inqool.dl4dh.krameriusplus.domain.entity.ModsMetadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication.Publication;

import java.util.ArrayList;
import java.util.List;

public class TeiHeaderFactory {

    public static TeiHeaderInput createHeaderInput(Publication publication) {
        TeiHeaderInput teiInput = new TeiHeaderInput();
        teiInput.setTitle(publication.getTitle());

        ModsMetadata modsMetadata = publication.getModsMetadata();

        if (modsMetadata != null) {
            teiInput.setPhysicalDescription(modsMetadata.getPhysicalDescription());

            if (modsMetadata.getName() != null) {
                teiInput.setAuthor(getAuthor(modsMetadata.getName()));
            }

            if (modsMetadata.getOriginInfo() != null) {
                teiInput.setOriginInfo(getOriginInfo(modsMetadata.getOriginInfo()));
            }

            teiInput.setIdentifiers(modsMetadata.getIdentifiers());
        }

        return teiInput;
    }

    private static TeiHeaderInput.TeiAuthor getAuthor(ModsMetadata.Name modsName) {
        TeiHeaderInput.TeiAuthor author = new TeiHeaderInput.TeiAuthor();
        author.setType(modsName.getType());
        author.setName(modsName.getNamePart());
        author.setIdentifier(modsName.getNameIdentifier());

        return author;
    }

    private static TeiHeaderInput.TeiOriginInfo getOriginInfo(ModsMetadata.OriginInfo modsOriginInfo) {
        TeiHeaderInput.TeiOriginInfo teiOriginInfo = new TeiHeaderInput.TeiOriginInfo();
        teiOriginInfo.setPublisher(modsOriginInfo.getPublisher());

        if (modsOriginInfo.getDateIssued() != null) {
            teiOriginInfo.setDate(getDate(modsOriginInfo.getDateIssued()));
        }

        if (modsOriginInfo.getPlaces() != null) {
            teiOriginInfo.setPlaces(getPlaces(modsOriginInfo.getPlaces()));
        }

        return teiOriginInfo;
    }

    private static String getDate(List<ModsMetadata.OriginInfo.DateIssued> dates) {
        for (var date : dates) {
            if (date.getPoint() == null) {
                return date.getValue();
            }
        }

        return null;
    }

    private static List<String> getPlaces(List<ModsMetadata.OriginInfo.Place> places) {
        List<String> result = new ArrayList<>();

        for (var place : places) {
            if (place.getType() != null && place.getType().equals("text")) {
                result.add(place.getValue());
            }
        }

        return result;
    }
}
