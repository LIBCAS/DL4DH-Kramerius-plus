package cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.alto.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TextBlockTypeDto extends BlockTypeDto {

    private List<TextLineDto> textLine;

    private String language;

    private String lang;

    @Getter
    @Setter
    public static class TextLineDto {

        private List<Object> stringAndSP;

        @Getter
        @Setter
        public static class SpDto {

            private String id;

            private Float height;

            private Float width;

            private Float hpos;

            private Float vpos;
        }

        @Getter
        @Setter
        public static class StringTypeDto {

            private String id;

            private List<Object> stylerefs;

            private List<Object> tagrefs;

            private Float height;

            private Float width;

            private Float hpos;

            private Float vpos;

            private String content;

            private List<String> style;

            private String substype;

            private String subscontent;

            private Float wc;

            private String cc;

            private Boolean cs;

            private String lang;
        }
    }
}
