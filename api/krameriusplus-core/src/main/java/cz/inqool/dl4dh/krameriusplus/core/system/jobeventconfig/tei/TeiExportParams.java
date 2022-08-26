package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.tei;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TeiExportParams {

    private List<UdPipeParam> udPipeParams = new ArrayList<>();

    private List<NameTagParam> nameTagParams = new ArrayList<>();

    private List<AltoParam> altoParams = new ArrayList<>();
}
