package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TeiExportParams {

    private List<String> udPipeParams = new ArrayList<>();

    private List<String> nameTagParams = new ArrayList<>();

    private List<String> altoParams = new ArrayList<>();
}
