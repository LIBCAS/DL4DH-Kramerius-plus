package cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.lindat.nametag;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.lindat.udpipe.Token;
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
