package cz.inqool.dl4dh.krameriusplus.core.user.request.entity;

import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "kplus_user_request_message")
public class UserRequestMessage extends DatedObject {

    @ManyToOne
    private UserRequest userRequest;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "kplus_user_request_message_files",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id"))
    private Set<FileRef> files;

    @ManyToOne
    private User author;

    @Lob
    private String message;
}
