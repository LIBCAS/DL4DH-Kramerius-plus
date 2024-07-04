package cz.inqool.dl4dh.krameriusplus.core.user.request.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import cz.inqool.dl4dh.krameriusplus.api.request.document.DocumentState;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DomainObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "kplus_user_request_part")
public class UserRequestPart extends DomainObject {

    @ManyToOne
    @JsonBackReference
    private UserRequest userRequest;

    private String publicationId;

    @Enumerated(EnumType.STRING)
    private DocumentState state = DocumentState.WAITING;

    @Lob
    @Column(columnDefinition = "text")
    private String note;

    private Instant stateUntil;
}
