package cz.inqool.dl4dh.krameriusplus.core.user.request;

import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DomainObject;
import cz.inqool.dl4dh.krameriusplus.core.file.FileRef;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "kplus_user_request_file")
public class UserRequestFile extends DomainObject {

    @ManyToOne
    private UserRequestMessage message;

    @OneToOne
    private FileRef fileRef;
}
