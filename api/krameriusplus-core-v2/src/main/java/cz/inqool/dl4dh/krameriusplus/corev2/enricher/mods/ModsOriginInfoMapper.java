package cz.inqool.dl4dh.krameriusplus.corev2.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsDateIssued;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsOriginInfo;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsPlace;
import cz.inqool.dl4dh.mods.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ModsOriginInfoMapper extends ModsMapperBase {

    @Mappings({
            @Mapping(target = "publisher", expression = "java(mapJaxbElements(element.getPlaceOrPublisherOrDateIssued(), \"publisher\"))"),
            @Mapping(target = "dateIssued", expression = "java(mapDate(findOne(element.getPlaceOrPublisherOrDateIssued(), \"dateIssued\")))"),
            @Mapping(target = "places", expression = "java(mapPlaces(element.getPlaceOrPublisherOrDateIssued()))"),
            @Mapping(target = "issuance", expression = "java(mapIssuance(findOne(element.getPlaceOrPublisherOrDateIssued(), \"issuance\")))")
    })
    ModsOriginInfo map(OriginInfoDefinition element);

    default List<ModsPlace> mapPlaces(List<JAXBElement<?>> elements) {
        if (elements == null || elements.isEmpty()) {
            return null;
        }

        List<ModsPlace> result = new ArrayList<>();

        for (JAXBElement<?> element : elements) {
            if (element.getValue() instanceof PlaceDefinition) {
                PlaceDefinition placeDefinition = (PlaceDefinition) element.getValue();
                if (placeDefinition.getPlaceTerm().isEmpty()) {
                    continue;
                } if (placeDefinition.getPlaceTerm().size() > 1) {
                    throw new IllegalStateException("Expected <place> element to have at most one <placeTerm> element, but found: " + placeDefinition.getPlaceTerm().size());
                }

                PlaceTermDefinition placeTermDefinition = placeDefinition.getPlaceTerm().get(0);

                ModsPlace modsPlace = new ModsPlace();
                modsPlace.setAuthority(placeTermDefinition.getAuthority());
                modsPlace.setType(placeTermDefinition.getType().value());
                modsPlace.setValue(placeTermDefinition.getValue());

                result.add(modsPlace);
            }
        }

        return result;
    }

    default ModsDateIssued mapDate(JAXBElement<?> element) {
        if (element == null) {
            return null;
        }

        if (!(element.getValue() instanceof DateDefinition)) {
            throw new IllegalStateException("Found <dateIssued> element, but it is not an instance of DateDefinition.");
        }
        DateDefinition dateDefinition = (DateDefinition) element.getValue();

        ModsDateIssued dateIssued = new ModsDateIssued();
        dateIssued.setEncoding(dateDefinition.getEncoding());
        dateIssued.setPoint(dateDefinition.getPoint());
        dateIssued.setValue(dateDefinition.getValue());

        return dateIssued;
    }

    default String mapIssuance(JAXBElement<?> issuanceElement) {
        if (issuanceElement == null) {
            return null;
        }

        if (!(issuanceElement.getValue() instanceof IssuanceDefinition)) {
            throw new IllegalStateException("Found <issuance> element, but it is not and instance of "
                    + IssuanceDefinition.class.getSimpleName());
        }

        return ((IssuanceDefinition) issuanceElement.getValue()).value();
    }
}
