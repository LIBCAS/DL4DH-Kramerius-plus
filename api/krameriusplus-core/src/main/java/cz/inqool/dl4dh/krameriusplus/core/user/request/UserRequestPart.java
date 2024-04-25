package cz.inqool.dl4dh.krameriusplus.core.user.request;

import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DomainObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "kplus_user_request_part")
public class UserRequestPart extends DomainObject {

    @ManyToOne
    private UserRequest userRequest;

    private String publicationId;

    @Enumerated(EnumType.STRING)
    private String state;

    @Lob
    private String note;

    private Instant stateUntil;
}
