package cz.inqool.dl4dh.krameriusplus.core.kramerius.v7;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DigitalObjectStructureDto {

    private ChildrenDto children;

    @Getter
    @Setter
    public static class ChildrenDto {

        @JsonAlias({"own"})
        private List<DigitalObjectRefDto> children;

        @Getter
        @Setter
        public static class DigitalObjectRefDto {

            private String pid;

        }
    }
}
