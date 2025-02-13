package cz.inqool.dl4dh.krameriusplus.core.user.request.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestState;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestType;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

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
    @Column(updatable = false)
    @Generated(GenerationTime.INSERT)
    private Integer identification;

    @OneToMany(cascade = ALL, mappedBy = "userRequest")
    @JsonManagedReference
    private Set<UserRequestPart> parts;

    @OneToMany(cascade = ALL, mappedBy = "userRequest")
    @OrderBy("created DESC ")
    @JsonManagedReference
    private List<UserRequestMessage> messages;

    @OneToMany(cascade = ALL, mappedBy = "userRequest")
    @OrderBy("created DESC ")
    @JsonManagedReference
    private List<UserRequestStateAudit> stateChanges;

    public String getRequestIdentification() {
        return created.atZone(ZoneId.systemDefault()).getYear() + "-" + String.format("%08d", identification);
    }
}
