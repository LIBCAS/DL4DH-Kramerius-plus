package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ObjectIdentifier {
    private String type;
    private String value;
}
