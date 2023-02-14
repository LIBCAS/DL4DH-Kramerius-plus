package cz.inqool.dl4dh.krameriusplus.core.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsDate;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsOriginInfo;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsPlace;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsPlaceTerm;
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
            @Mapping(target = "publishers", expression = "java(extractStringsFromListOfJAXBElements(element.getPlaceOrPublisherOrDateIssued(), \"publisher\"))"),
            @Mapping(target = "datesIssued", expression = "java(mapDates(element.getPlaceOrPublisherOrDateIssued()))"),
            @Mapping(target = "places", expression = "java(mapPlaces(element.getPlaceOrPublisherOrDateIssued()))"),
            @Mapping(target = "issuances", expression = "java(mapIssuance(element.getPlaceOrPublisherOrDateIssued()))")
    })
    ModsOriginInfo map(OriginInfoDefinition element);

    default List<ModsPlace> mapPlaces(List<JAXBElement<?>> elements) {
        if (elements == null || elements.isEmpty()) {
            return new ArrayList<>();
        }

        List<ModsPlace> result = new ArrayList<>();

        for (JAXBElement<?> element : elements) {
            if (element.getValue() instanceof PlaceDefinition) {
                PlaceDefinition placeDefinition = (PlaceDefinition) element.getValue();
                if (placeDefinition.getPlaceTerm().isEmpty()) {
                    continue;
                }

                ModsPlace modsPlace = new ModsPlace();
                for (PlaceTermDefinition placeTermDefinition : placeDefinition.getPlaceTerm()) {
                    ModsPlaceTerm placeTerm = new ModsPlaceTerm();
                    placeTerm.setAuthority(placeTermDefinition.getAuthority());
                    placeTerm.setValue(placeTermDefinition.getValue());
                    placeTerm.setType(placeTermDefinition.getType() == null ? null : placeTermDefinition.getType().value());

                    modsPlace.getPlaceTerms().add(placeTerm);
                }

                result.add(modsPlace);
            }
        }

        return result;
    }

    default List<ModsDate> mapDates(List<JAXBElement<?>> elements) {
        if (elements == null || elements.isEmpty()) {
            return null;
        }

        List<ModsDate> result = new ArrayList<>();
        for (JAXBElement<?> element : elements) {
            if (element.getValue() instanceof DateDefinition) {
                DateDefinition dateDefinition = (DateDefinition) element.getValue();

                ModsDate dateIssued = new ModsDate();
                dateIssued.setEncoding(dateDefinition.getEncoding());
                dateIssued.setPoint(dateDefinition.getPoint());
                dateIssued.setValue(dateDefinition.getValue());
                result.add(dateIssued);
            }
        }

        return result;
    }

    default List<String> mapIssuance(List<JAXBElement<?>> elements) {
        List<String> result = new ArrayList<>();

        for (JAXBElement<?> element : elements) {
            if (element.getValue() instanceof IssuanceDefinition) {
                result.add(((IssuanceDefinition) element.getValue()).value());
            }
        }

        return result;
    }
}
