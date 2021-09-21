package cz.inqool.dl4dh.krameriusplus.domain.dao.repo.filter;

import cz.inqool.dl4dh.krameriusplus.domain.dao.ExportFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class Params {

    private List<FilterParam.NameTagParam> nameTagParams = new ArrayList<>();

    private List<FilterParam.UDPipeParam> udPipeParams = new ArrayList<>();

    private List<FilterParam.NameTagParam> altoParams = new ArrayList<>();

    private ExportFormat format;

    public String getDbQuery() {
        return "{ " +
                udPipeParams
                        .stream()
                        .map(param -> param.getDbFieldName() + ": 1")
                        .collect(Collectors.joining(", ")) +
                " }";
    }
}
