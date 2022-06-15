package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Set of parameters for exporting in TEI format.")
public class TeiParams extends Params {

    @Schema(description = "Set of parameters that determine which UDPipe metadata tags will be included in TEI. " +
            "If no parameters are specified, every metadata tag will be included.", //TODO: Document "any illegal character" behaviour
            allowableValues = {"n", "lemma", "pos", "msd", "join"})
    private List<String> udPipeParams = new ArrayList<>();

    @Schema(description = "Set of parameters that determine which NameTag metadata tags will be included in TEI. " +
            "If no parameters are specified, every metadata tag will be included.",
            allowableValues = {"a", "g", "i", "m", "n", "o", "p", "t"})
    private List<String> nameTagParams = new ArrayList<>();

    @Schema(description = "Set of parameters that determine which ALTO metadata tags will be included in TEI. " +
            "If no parameters are specified, every metadata tag will be included.",
            allowableValues = {"height", "width", "vpos", "hpos"})
    private List<String> altoParams = new ArrayList<>();
}
