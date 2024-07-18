package cz.inqool.dl4dh.krameriusplus.core.user.request.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestState;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "kplus_user_request_state_audit")
public class UserRequestStateAudit extends DatedObject {

    @ManyToOne
    @JsonManagedReference
    private UserRequest userRequest;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "state_before")
    private UserRequestState before;

    @Enumerated(EnumType.STRING)
    @Column(name = "state_after")
    private UserRequestState after;
}
