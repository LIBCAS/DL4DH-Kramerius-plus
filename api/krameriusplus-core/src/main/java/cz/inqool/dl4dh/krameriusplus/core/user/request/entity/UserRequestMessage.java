package cz.inqool.dl4dh.krameriusplus.core.user.request.entity;

import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "kplus_user_request_message")
public class UserRequestMessage extends DatedObject {

    @ManyToOne
    @JoinColumn(name = "user_request_id")
    private UserRequest userRequest;

    @ManyToOne
    private User author;

    @Lob
    private String message;
}
