package cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.alto.dto;

import cz.inqool.dl4dh.alto.Alto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AltoDto {

    private LayoutDto layout;

    private Alto.Description description;

    @Getter
    @Setter
    public static class LayoutDto {

        private List<PageDto> page;

        @Getter
        @Setter
        public static class PageDto {

            private PageSpaceTypeDto printSpace;
        }
    }
}
