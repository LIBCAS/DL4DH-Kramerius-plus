package cz.inqool.dl4dh.krameriusplus.domain.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class ModsMetadata {

    private List<TitleInfo> titleInfos = new ArrayList<>();

    private Name name;

    private String genre;

    private PhysicalDescription physicalDescription;

    private OriginInfo originInfo;

    private List<Identifier> identifiers = new ArrayList<>();

    @Getter
    @AllArgsConstructor
    public static class TitleInfo {
        private final List<String> title;
        private final String type;
        private final String displayLabel;
    }

    @Getter
    @AllArgsConstructor
    public static class Name {
        private final String type;
        private final String namePart;
        private final String nameIdentifier;
    }

    @Getter
    @AllArgsConstructor
    public static class PhysicalDescription {
        private final String extent;
    }

    @Getter
    @AllArgsConstructor
    public static class OriginInfo {
        private final String publisher;
        private final List<DateIssued> dateIssued;
        private final List<Place> places;

        @Getter
        @AllArgsConstructor
        @EqualsAndHashCode
        public static class DateIssued {
            private final String encoding;
            private final String point;
            private final String value;
        }

        @Getter
        @AllArgsConstructor
        @EqualsAndHashCode
        public static class Place {
            private final String authority;
            private final String type;
            private final String value;
        }
    }

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class Identifier {
        private final String type;
        private final Boolean invalid;
        private final String value;
    }
}
