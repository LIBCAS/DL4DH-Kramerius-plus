package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter.Sorting;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

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

    public TeiParams(Params params) {
        paging = params.getPaging();
        sorting = params.getSorting();
        filters = params.getFilters();
        includeFields = params.getIncludeFields();
        excludeFields = params.getExcludeFields();
    }

    public Params cleanForTei() {
        sorting = new ArrayList<>();
        sorting.add(new Sorting("index", Sort.Direction.ASC));

        includeFields = new ArrayList<>();
        includeFields("id", "teiBodyFileId");

        return this;
    }
}
