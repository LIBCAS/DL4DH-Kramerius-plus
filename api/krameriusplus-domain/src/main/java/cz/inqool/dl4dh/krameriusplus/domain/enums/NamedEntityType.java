package cz.inqool.dl4dh.krameriusplus.domain.enums;

import lombok.Getter;

/**
 * @author Norbert Bodnar
 */
@Getter
public enum NamedEntityType {
    NUMBERS_IN_ADDRESSES('a'),
    GEOGRAPHICAL_NAMES('g'),
    INSTITUTIONS('i'),
    MEDIA_NAMES('m'),
    NUMBER_EXPRESSIONS('n'),
    ARTIFACT_NAMES('o'),
    PERSONAL_NAMES('p'),
    TIME_EXPRESSIONS('t');

    private final char typeSymbol;

    NamedEntityType(char c) {
        this.typeSymbol = c;
    }

    public static NamedEntityType fromString(String stringValue) {
        if (stringValue == null || stringValue.isEmpty()) {
            throw new IllegalArgumentException("Missing argument value");
        }

        String entityType = stringValue.toLowerCase();

        char firstChar = entityType.charAt(0);

        for (NamedEntityType namedEntityType : NamedEntityType.values()) {
            if (namedEntityType.getTypeSymbol() == firstChar) {
                return namedEntityType;
            }
        }

        throw new IllegalArgumentException("Cannot construct NamedEntityType enum from value: " + stringValue);
    }
}
