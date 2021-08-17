package cz.inqool.dl4dh.krameriusplus.metadata;

import cz.inqool.dl4dh.krameriusplus.domain.entity.ModsMetadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.ModsMetadata.*;
import cz.inqool.dl4dh.krameriusplus.domain.entity.ModsMetadata.OriginInfo.DateIssued;
import cz.inqool.dl4dh.krameriusplus.domain.entity.ModsMetadata.OriginInfo.Place;
import cz.inqool.dl4dh.mods.*;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;

public enum ModsGroupType {
    TITLE(TitleInfoDefinition.class) {

        @Override
        public void addAttribute(Object modsGroup, ModsMetadata modsMetadata) {
            TitleInfoDefinition titleInfo = (TitleInfoDefinition) modsGroup;

            List<String> titleNames = extractTitleNames(titleInfo.getTitleOrSubTitleOrPartNumber());

            modsMetadata.getTitleInfos().add(new TitleInfo(
                    titleNames,
                    titleInfo.getTypeAtt(),
                    titleInfo.getDisplayLabel()));
        }

        private List<String> extractTitleNames(List<Object> childElements) {
            List<String> result = new ArrayList<>();
            for (Object xmlElement : childElements) {
                if (xmlElement instanceof JAXBElement) {
                    JAXBElement<?> jxb = (JAXBElement<?>) xmlElement;
                    if (jxb.getValue() instanceof StringPlusLanguage) {
                        result.add(((StringPlusLanguage) jxb.getValue()).getValue());
                    }
                }
            }

            return result;
        }
    },
    NAME(NameDefinition.class) {

        @Override
        public void addAttribute(Object modsGroup, ModsMetadata modsMetadata) {
            NameDefinition nameDefinition = (NameDefinition) modsGroup;

            List<JAXBElement<?>> children = nameDefinition.getNamePartOrDisplayFormOrAffiliation();

            String type = nameDefinition.getTypeAtt();
            String namePart = extractNamePart(children);
            String nameIdentifier = extractNameIdentifier(children);

            modsMetadata.setName(new Name(type, namePart, nameIdentifier));
        }

        private String extractNameIdentifier(List<JAXBElement<?>> childElements) {
            for (JAXBElement<?> xmlElement : childElements) {
                if (xmlElement.getValue() instanceof IdentifierDefinition) {
                    return ((IdentifierDefinition) xmlElement.getValue()).getValue();
                }
            }

            return null;
        }

        private String extractNamePart(List<JAXBElement<?>> childElements) {
            for (JAXBElement<?> xmlElement : childElements) {
                if (xmlElement.getValue() instanceof NamePartDefinition) {
                    return ((NamePartDefinition) xmlElement.getValue()).getValue();
                }
            }

            return null;
        }
    },
    PHYSICAL_DESCRIPTION(PhysicalDescriptionDefinition.class) {

        @Override
        public void addAttribute(Object modsGroup, ModsMetadata modsMetadata) {
            PhysicalDescriptionDefinition physicalDescriptionDefinition = (PhysicalDescriptionDefinition) modsGroup;

            String extent = extractExtent(physicalDescriptionDefinition.getFormOrReformattingQualityOrInternetMediaType());

            modsMetadata.setPhysicalDescription(new PhysicalDescription(extent));
        }

        private String extractExtent(List<Object> childElements) {
            for (Object xmlElement : childElements) {
                if (xmlElement instanceof Extent) {
                    return ((Extent) xmlElement).getValue();
                }
            }

            return null;
        }
    },
    ORIGIN_INFO(OriginInfoDefinition.class) {

        @Override
        public void addAttribute(Object modsGroup, ModsMetadata modsMetadata) {
            OriginInfoDefinition originInfoDefinition = (OriginInfoDefinition) modsGroup;

            List<JAXBElement<?>> childElements = originInfoDefinition.getPlaceOrPublisherOrDateIssued();

            String publisher = extractPublisher(childElements);
            List<DateIssued> datesIssued = extractDatesIssued(childElements);
            List<Place> places = extractPlaces(childElements);

            modsMetadata.setOriginInfo(new OriginInfo(publisher, datesIssued, places));
        }

        private String extractPublisher(List<JAXBElement<?>> childElements) {
            for (JAXBElement<?> xmlElement : childElements) {
                if (xmlElement.getValue() instanceof PublisherDefinition) {
                    return ((PublisherDefinition) xmlElement.getValue()).getValue();
                }
            }

            return null;
        }

        private List<DateIssued> extractDatesIssued(List<JAXBElement<?>> childElements) {
            List<DateIssued> result = new ArrayList<>();

            for (JAXBElement<?> xmlElement : childElements) {
                if (xmlElement.getValue() instanceof DateDefinition) {
                    DateDefinition xmlDateElement = (DateDefinition) xmlElement.getValue();
                    String encoding = xmlDateElement.getEncoding();
                    String point = xmlDateElement.getPoint();
                    String date = xmlDateElement.getValue();

                    result.add(new DateIssued(encoding, point, date));
                }
            }

            return result;
        }

        private List<Place> extractPlaces(List<JAXBElement<?>> childElements) {
            List<Place> result = new ArrayList<>();

            for (JAXBElement<?> xmlElement : childElements) {
                if (xmlElement.getValue() instanceof PlaceDefinition) {
                    PlaceDefinition place = (PlaceDefinition) xmlElement.getValue();

                    for (PlaceTermDefinition placeTerm : place.getPlaceTerm()) {
                        String authority = placeTerm.getAuthority();
                        String type = null;
                        if (placeTerm.getType() != null) {
                            type = placeTerm.getType().value();
                        }
                        String value = placeTerm.getValue();
                        result.add(new Place(authority, type, value));
                    }
                }
            }

            return result;
        }

    },
    IDENTIFIER(IdentifierDefinition.class) {

        @Override
        public void addAttribute(Object modsGroup, ModsMetadata modsMetadata) {
            IdentifierDefinition identifierDefinition = (IdentifierDefinition) modsGroup;

            modsMetadata.getIdentifiers().add(new Identifier(
                    identifierDefinition.getType(),
                    extractInvalidAsBoolean(identifierDefinition),
                    identifierDefinition.getValue()));
        }

        private Boolean extractInvalidAsBoolean(IdentifierDefinition identifierDefinition) {
            return identifierDefinition.getInvalid() == null ? Boolean.FALSE :
                    identifierDefinition.getInvalid().equals("yes") ? Boolean.TRUE : Boolean.FALSE;
        }
    };

    private final Class<?> clazz;

    ModsGroupType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public static ModsGroupType from(Class<?> clazz) {
        for (ModsGroupType modsGroupType : ModsGroupType.values()) {
            if (modsGroupType.clazz.equals(clazz)) {
                return modsGroupType;
            }
        }

        return null;
    }

    public abstract void addAttribute(Object modsGroup, ModsMetadata modsMetadata);
}
