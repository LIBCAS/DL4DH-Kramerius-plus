package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page;

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
    TIME_EXPRESSIONS('t'),
    COMPLEX_PERSON_NAMES('P'),
    COMPLEX_TIME_EXPRESSION('T'),
    COMPLEX_ADDRESS_EXPRESSION('A'),
    COMPLEX_BIBLIO_EXPRESSION('C');

    private final char typeSymbol;

    NamedEntityType(char ch) {
        this.typeSymbol = ch;
    }

    public static NamedEntityType fromString(String stringValue) {
        if (stringValue == null || stringValue.isEmpty()) {
            throw new IllegalArgumentException("Missing argument value");
        }

        char firstChar = stringValue.charAt(0);

        for (NamedEntityType namedEntityType : NamedEntityType.values()) {
            if (namedEntityType.getTypeSymbol() == firstChar) {
                return namedEntityType;
            }
        }

        throw new IllegalArgumentException("Cannot construct NamedEntityType enum from value: " + stringValue);
    }
}
