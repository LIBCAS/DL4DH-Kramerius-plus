package cz.inqool.dl4dh.krameriusplus.core.user.request.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.temporal.ChronoField;
import java.util.Set;

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

    @JsonIgnore
    @GeneratedValue(generator = "identification_generator")
    @SequenceGenerator(name = "identification_generator",
            sequenceName = "kplus_user_request_number_SEQ",
            allocationSize = 1)
    private Integer identification;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_request_id")
    private Set<UserRequestPart> parts;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_request_id")
    private Set<UserRequestMessage> messages;

    public String getRequestIdentification() {
        return created.get(ChronoField.YEAR) + String.format("%08d", identification);
    }
}
