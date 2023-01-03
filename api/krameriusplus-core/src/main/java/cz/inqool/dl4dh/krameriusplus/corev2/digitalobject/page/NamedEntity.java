package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class NamedEntity {

    private List<Token> tokens = new ArrayList<>();

    private String entityType;
}
