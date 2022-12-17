package cz.inqool.dl4dh.krameriusplus.corev2.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsDateIssued;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsOriginInfo;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsPlace;
import cz.inqool.dl4dh.mods.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import javax.xml.bind.JAXBElement;

@Mapper
public interface ModsOriginInfoMapper extends ModsMapperBase {

    @Mappings({
            @Mapping(target = "publisher", expression = "java(mapJaxbElements(element.getPlaceOrPublisherOrDateIssued(), \"publisher\"))"),
            @Mapping(target = "dateIssued", expression = "java(mapDate(findOne(element.getPlaceOrPublisherOrDateIssued(), \"dateIssued\")))"),
            @Mapping(target = "place", expression = "java(mapPlace(findOne(element.getPlaceOrPublisherOrDateIssued(), \"place\")))"),
            @Mapping(target = "issuance", expression = "java(mapIssuance(findOne(element.getPlaceOrPublisherOrDateIssued(), \"issuance\")))")
    })
    ModsOriginInfo map(OriginInfoDefinition element);

    default ModsPlace mapPlace(JAXBElement<?> element) {
        if (!(element.getValue() instanceof PlaceDefinition)) {
            throw new IllegalStateException("Found <place> element, but it is not an instance of PlaceDefinition.");
        }
        PlaceDefinition placeDefinition = (PlaceDefinition) element.getValue();

        ModsPlace modsPlace = new ModsPlace();
        if (placeDefinition.getPlaceTerm().isEmpty()) {
            return modsPlace;
        }
        if (placeDefinition.getPlaceTerm().size() > 1) {
            throw new IllegalStateException("Expected at most one placeTermDefinition inside of Place element, " +
                    "but found: " + placeDefinition.getPlaceTerm().size());
        }

        PlaceTermDefinition placeTermDefinition = placeDefinition.getPlaceTerm().get(0);
        modsPlace.setAuthority(placeTermDefinition.getAuthority());
        modsPlace.setType(placeTermDefinition.getType() != null ? placeTermDefinition.getType().value() : null);
        modsPlace.setValue(placeTermDefinition.getValue());

        return modsPlace;
    }

    default ModsDateIssued mapDate(JAXBElement<?> element) {
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
