package cz.inqool.dl4dh.krameriusplus.service.export.filter;

import cz.inqool.dl4dh.krameriusplus.service.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.service.export.filter.FilterParam;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Params {

    private List<FilterParam.NameTagParam> nameTagParams = new ArrayList<>();

    private List<FilterParam.UDPipeParam> udPipeParams = new ArrayList<>();

    private List<FilterParam.NameTagParam> altoParams = new ArrayList<>();

    private ExportFormat format;
}
