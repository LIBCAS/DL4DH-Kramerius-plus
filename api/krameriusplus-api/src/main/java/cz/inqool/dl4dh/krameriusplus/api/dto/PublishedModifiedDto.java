package cz.inqool.dl4dh.krameriusplus.api.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
public class PublishedModifiedDto {

    @NotNull
    private Instant publishedModifiedAfter;
}
