package cz.inqool.dl4dh.krameriusplus.service.export.filter;

import lombok.Getter;

public interface FilterParam {

    String getTeiParam();

    @Getter
    enum UDPipeParam implements FilterParam {
        POSITION("n"),
        LEMMA("lemma"),
        U_POS_TAG("pos"),
        FEATS("msd"),
        JOIN("join");

        private final String teiParam;

        UDPipeParam(String teiParam) {
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

        private final String teiParam;

        NameTagParam(String teiParam) {
            this.teiParam = teiParam;
        }
    }

    @Getter
    enum AltoParam implements FilterParam {
        WIDTH("width"),
        HEIGHT("height"),
        VERTICAL_POS("vpos"),
        HORIZONTAL_POS("hpos");

        private final String teiParam;

        AltoParam(String teiParam) {
            this.teiParam = teiParam;
        }
    }
}
