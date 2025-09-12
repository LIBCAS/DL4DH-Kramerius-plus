package cz.inqool.dl4dh.krameriusplus.api.publication.paradata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProcessedBy {
    private String identifier;
    private String version;
    private String from;
    private String to;
    private String when;
    private String label;
}
