package cz.inqool.dl4dh.krameriusplus.core.user.request;

import cz.inqool.dl4dh.krameriusplus.api.user.UserType;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DatedObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "kplus_user_request_message")
public class UserRequestMessage extends DatedObject {

    private String username;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Lob
    private String message;
}
