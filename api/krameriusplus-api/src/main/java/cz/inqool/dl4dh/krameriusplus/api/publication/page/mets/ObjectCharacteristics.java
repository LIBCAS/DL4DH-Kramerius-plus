package cz.inqool.dl4dh.krameriusplus.api.publication.page.mets;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
public class ObjectCharacteristics {

    private BigInteger compositionLevel;

    private Fixity fixity;

    private long size;

    private Format format;

    private CreatingApplication creatingApplication;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Fixity {

        private String messageDigestAlgorithm;
        private String messageDigest;
        private String messageDigestOriginator;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Format {

        private FormatDesignation designation;
        private FormatRegistry registry;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class FormatDesignation {
            private String formatName;
            private String formatVersion;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class FormatRegistry {
            private String formatRegistryName;
            private String formatRegistryKey;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CreatingApplication {
        private String creatingApplicationName;
        private String creatingApplicationVersion;
        private String dateCreatedByApplication;
    }
}
