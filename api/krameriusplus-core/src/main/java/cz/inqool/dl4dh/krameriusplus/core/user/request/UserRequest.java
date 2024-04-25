package cz.inqool.dl4dh.krameriusplus.core.user.request;

import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestType;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "kplus_user_request")
public class UserRequest extends DatedObject {

    @Enumerated(EnumType.STRING)
    private UserRequestType requestType;

    @ManyToOne
    private User user;

    // TODO: generator
    private String identification;
}
