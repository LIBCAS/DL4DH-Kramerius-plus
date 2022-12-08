package cz.inqool.dl4dh.krameriusplus.corev2.request.export.export;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.corev2.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.TreeMap;

@Getter
@Setter
@Entity
@Table(name = "kplus_export")
public class Export extends DatedObject {

    @NotNull
    private String publicationId;

    private String publicationTitle;

    @OneToOne
    private FileRef fileRef;

    @ManyToOne
    private Export parent;

    @Enumerated(EnumType.STRING)
    private ExportFormat format;

    @NotNull
    @Column(name = "export_order")
    private Long order;

    @OneToOne
    @NotNull
    private KrameriusJobInstance exportJob;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
    @MapKey(name = "order")
    private Map<Long, Export> children = new TreeMap<>();
}
