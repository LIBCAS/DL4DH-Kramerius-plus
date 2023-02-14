package cz.inqool.dl4dh.krameriusplus.api.publication.mods;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ModsOriginInfo {

    private String eventType;

    private List<String> publishers = new ArrayList<>();

    private List<ModsDate> datesIssued = new ArrayList<>();

    private List<ModsPlace> places = new ArrayList<>();

    private List<String> issuances = new ArrayList<>();
}
