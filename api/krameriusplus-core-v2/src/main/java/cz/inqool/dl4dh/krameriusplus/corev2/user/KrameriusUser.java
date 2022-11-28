package cz.inqool.dl4dh.krameriusplus.corev2.user;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DatedObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class KrameriusUser extends DatedObject {

    @Column(nullable = false)
    private String username;
}
