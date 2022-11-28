package cz.inqool.dl4dh.krameriusplus.corev2.request;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.corev2.user.KrameriusUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@MappedSuperclass
public abstract class Request extends DatedObject {

    @ManyToOne
    private KrameriusUser owner;

    private String name;

    @ElementCollection
    @CollectionTable(name = "request_publication_id",
            joinColumns = @JoinColumn(name = "request_id"))
    private List<String> publicationIds = new ArrayList<>();
}
