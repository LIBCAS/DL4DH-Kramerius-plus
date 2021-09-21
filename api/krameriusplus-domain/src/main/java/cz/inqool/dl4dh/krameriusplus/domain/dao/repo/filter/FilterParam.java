package cz.inqool.dl4dh.krameriusplus.domain.dao.repo.filter;

import lombok.Getter;

public interface FilterParam {

    String getDbFieldName();

    @Getter
    enum UDPipeParam implements FilterParam {
        POSITION("p", "n"),
        LEMMA("l", "lemma"),
        U_POS_TAG("u","pos"),
        FEATS("f", "msd"),
        JOIN(null, "join");

        private final static String COMMON_DB_FIELD_NAME_PREFIX = "tokens.lm.";
        @Getter
        private final String dbFieldName;
        private final String teiParam;

        UDPipeParam(String dbFieldName, String teiParam) {
            this.dbFieldName = COMMON_DB_FIELD_NAME_PREFIX + dbFieldName;
            this.teiParam = teiParam;
        }
    }

    @Getter
    enum NameTagParam implements FilterParam {
        NUMBERS_IN_ADDRESSES("a"),
        GEOGRAPHICAL_NAMES("g"),
        INSTITUTIONS("i"),
        MEDIA_NAMES("m"),
        NUMBER_EXPRESSIONS("n"),
        ARTIFACT_NAMES("o"),
        PERSONAL_NAMES("p"),
        TIME_EXPRESSIONS("t");

        private final static String COMMON_DB_FIELD_NAME_PREFIX = "tokens.ntm";
        @Getter
        private final String dbFieldName;
        private final String teiParam;

        NameTagParam(String teiParam) {
            this.dbFieldName = COMMON_DB_FIELD_NAME_PREFIX;
            this.teiParam = teiParam;
        }
    }

    @Getter
    enum AltoParam implements FilterParam {
        WIDTH("width"),
        HEIGHT("height"),
        VERTICAL_POS("vpos"),
        HORIZONTAL_POS("hpos");

        private final static String COMMON_DB_FIELD_NAME_PREFIX = "tokens.am.";
        @Getter
        private final String dbFieldName;
        private final String teiParam;

        AltoParam(String teiParam) {
            this.dbFieldName = COMMON_DB_FIELD_NAME_PREFIX + teiParam;
            this.teiParam = teiParam;
        }
    }
}
