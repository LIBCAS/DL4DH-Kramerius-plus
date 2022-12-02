package cz.inqool.dl4dh.krameriusplus.api.publication.mods;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ModsOriginInfo {

    private String publisher;

    private List<ModsDateIssued> dateIssued = new ArrayList<>();

    private List<ModsPlace> places = new ArrayList<>();
}
