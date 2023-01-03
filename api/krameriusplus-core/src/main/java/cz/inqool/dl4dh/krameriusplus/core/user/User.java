package cz.inqool.dl4dh.krameriusplus.core.user;

import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DatedObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "kplus_user")
public class User extends DatedObject {

    @Column(nullable = false)
    private String username;
}
