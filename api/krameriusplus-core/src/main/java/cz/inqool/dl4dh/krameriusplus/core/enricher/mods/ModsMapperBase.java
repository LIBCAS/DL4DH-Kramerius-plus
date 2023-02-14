package cz.inqool.dl4dh.krameriusplus.core.enricher.mods;

import cz.inqool.dl4dh.mods.StringPlusLanguage;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;

public interface ModsMapperBase {

    default JAXBElement<?> findOne(List<JAXBElement<?>> elements, String tagName) {
        List<JAXBElement<?>> result = new ArrayList<>();

        for (JAXBElement<?> element : elements) {
            if (!tagName.equals(element.getName().getLocalPart())) {
                continue;
            }

            result.add(element);
        }

        if (result.size() > 1) {
            throw new IllegalStateException("Expected JAXBElement<?> to contain at most one subElement with tagName: "
                    + tagName + ", but found: " + result.size());
        }

        return result.isEmpty() ? null : result.get(0);
    }

    default String mapJaxbElements(List<JAXBElement<?>> elements, String tagName) {
        JAXBElement<?> element = findOne(elements, tagName);

        if (element == null) {
            return null;
        }

        if (!(element.getValue() instanceof StringPlusLanguage)) {
            throw new IllegalStateException("Found element with tag:" + tagName + ", but it is not an instance of " +
                    "StringPlusLanguage element and therefore cannot be casted to type String.");
        }

        return ((StringPlusLanguage) element.getValue()).getValue();
    }

    default String mapJaxbElementsObjects(List<Object> elements, String tagName) {
        List<JAXBElement<?>> result = new ArrayList<>();

        for (Object object : elements) {
            if (!(object instanceof JAXBElement<?>)) {
                continue;
            }

            result.add((JAXBElement<?>) object);
        }

        return mapJaxbElements(result, tagName);
    }

    default List<String> extractStringsFromListOfObjects(List<Object> elements, String elementName) {
        List<String> result = new ArrayList<>();
        for (Object element : elements) {
            if (element instanceof StringPlusLanguage) {
                result.add(((StringPlusLanguage) element).getValue());
            } else if (element instanceof JAXBElement<?> && ((JAXBElement<?>) element).getValue() instanceof StringPlusLanguage) {
                JAXBElement<?> jaxbElement = (JAXBElement<?>) element;

                if (elementName != null) {
                    if (jaxbElement.getName().getLocalPart().equals(elementName)) {
                        result.add(((StringPlusLanguage) jaxbElement.getValue()).getValue());
                    }
                } else {
                    result.add(((StringPlusLanguage) jaxbElement.getValue()).getValue());
                }
            }
        }

        return result;
    }

    default List<String> extractStringsFromListOfJAXBElements(List<JAXBElement<?>> elements, String elementName) {
        List<String> result = new ArrayList<>();

        for (JAXBElement<?> element : elements) {
            if (element.getValue() instanceof StringPlusLanguage) {
                if (elementName != null) {
                    if (element.getName().getLocalPart().equals(elementName)) {
                        result.add(((StringPlusLanguage) element.getValue()).getValue());
                    }
                } else {
                    result.add(((StringPlusLanguage) element.getValue()).getValue());
                }
            }
        }

        return result;
    }
}
