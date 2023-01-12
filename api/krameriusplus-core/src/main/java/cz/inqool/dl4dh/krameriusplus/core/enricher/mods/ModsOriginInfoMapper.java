package cz.inqool.dl4dh.krameriusplus.core.enricher.mods;

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
            @Mapping(target = "publishers", expression = "java(mapPublishInfo(element.getPlaceOrPublisherOrDateIssued()))"),
            @Mapping(target = "datesIssued", expression = "java(mapDates(element.getPlaceOrPublisherOrDateIssued()))"),
            @Mapping(target = "places", expression = "java(mapPlaces(element.getPlaceOrPublisherOrDateIssued()))"),
            @Mapping(target = "issuances", expression = "java(mapIssuances(element.getPlaceOrPublisherOrDateIssued()))")
    })
    ModsOriginInfo map(OriginInfoDefinition element);

    default List<String> mapPublishInfo(List<JAXBElement<?>> elements) {
        if (elements == null || elements.isEmpty()) {
            return null;
        }

        List<String> result = new ArrayList<>();

        for (JAXBElement<?> element : elements) {
            if (element.getValue() instanceof PublisherDefinition) {
                PublisherDefinition publisherDefinition = (PublisherDefinition) element.getValue();
                result.add(publisherDefinition.getValue());
            }
        }

        return result;

    }

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
                }
                if (placeDefinition.getPlaceTerm().size() > 1) {
                    throw new IllegalStateException("Expected <place> element to have at most one <placeTerm> element, but found: " + placeDefinition.getPlaceTerm().size());
                }

                PlaceTermDefinition placeTermDefinition = placeDefinition.getPlaceTerm().get(0);

                ModsPlace modsPlace = new ModsPlace();
                modsPlace.setAuthority(placeTermDefinition.getAuthority());
                modsPlace.setType(placeTermDefinition.getType() == null ? null : placeTermDefinition.getType().value());
                modsPlace.setValue(placeTermDefinition.getValue());

                result.add(modsPlace);
            }
        }

        return result;
    }

    default List<String> mapIssuances(List<JAXBElement<?>> elements) {
        if (elements == null || elements.isEmpty()) {
            return null;
        }

        List<String> issuances = new ArrayList<>();
        for (JAXBElement<?> element : elements) {
            if (element.getValue() instanceof IssuanceDefinition) {
                issuances.add(((IssuanceDefinition) element.getValue()).value());
            }
        }


        return issuances;
    }

    default List<ModsDateIssued> mapDates(List<JAXBElement<?>> elements) {
        if (elements == null || elements.isEmpty()) {
            return null;
        }

        List<ModsDateIssued> result = new ArrayList<>();
        for (JAXBElement<?> element : elements) {
            if (element.getValue() instanceof DateDefinition) {
                DateDefinition dateDefinition = (DateDefinition) element.getValue();

                ModsDateIssued dateIssued = new ModsDateIssued();
                dateIssued.setEncoding(dateDefinition.getEncoding());
                dateIssued.setPoint(dateDefinition.getPoint());
                dateIssued.setValue(dateDefinition.getValue());
                result.add(dateIssued);
            }
        }

        return result;
    }
}
