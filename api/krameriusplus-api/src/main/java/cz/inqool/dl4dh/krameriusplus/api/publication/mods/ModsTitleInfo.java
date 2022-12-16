package cz.inqool.dl4dh.krameriusplus.api.publication.mods;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ModsTitleInfo {

    private List<String> titles = new ArrayList<>();

    private String type;

    private String displayLabel;
}
