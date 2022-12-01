package cz.inqool.dl4dh.krameriusplus.corev2.request;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.corev2.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.TreeMap;

@Getter
@Setter
@Entity
@Table(name = "kplus_request")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Request extends DatedObject {

    @ManyToOne
    @NotNull
    private User owner;

    private String name;

    @ElementCollection
    @CollectionTable(name = "kplus_request_publication_id",
            joinColumns = @JoinColumn(name = "request_id"))
    @Column(name = "publication_id")
    @MapKeyColumn(name = "publication_id_order")
    private Map<Long, String> publicationIds = new TreeMap<>();
}
