package cz.inqool.dl4dh.krameriusplus.api.user.request.document;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChangeDocumentStatesDto {

    List<String> documentIds;

    DocumentState state;



    boolean forceTransitions = false;
}
