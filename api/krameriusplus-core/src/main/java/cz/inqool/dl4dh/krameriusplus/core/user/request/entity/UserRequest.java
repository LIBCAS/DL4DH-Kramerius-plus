package cz.inqool.dl4dh.krameriusplus.core.user.request.entity;

import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestState;
import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestType;
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
@Table(name = "kplus_user_request")
public class UserRequest extends DatedObject {

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type")
    private UserRequestType type;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private UserRequestState state = UserRequestState.CREATED;

    private String identification;


}
