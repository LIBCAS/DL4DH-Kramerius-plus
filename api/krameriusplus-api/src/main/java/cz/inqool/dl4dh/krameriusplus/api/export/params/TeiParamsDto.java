package cz.inqool.dl4dh.krameriusplus.api.export.params;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TeiParamsDto {

    private List<UdPipeParam> udPipeParams = new ArrayList<>();

    private List<NameTagParam> nameTagParams = new ArrayList<>();

    private List<AltoParam> altoParams = new ArrayList<>();
}
