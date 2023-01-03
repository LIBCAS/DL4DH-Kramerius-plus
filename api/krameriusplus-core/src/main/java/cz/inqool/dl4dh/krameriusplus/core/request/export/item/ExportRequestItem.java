package cz.inqool.dl4dh.krameriusplus.core.request.export.item;

import cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DomainObject;
import cz.inqool.dl4dh.krameriusplus.core.request.export.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequest;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "kplus_export_request_item")
public class ExportRequestItem extends DomainObject {

    @NotNull
    private String publicationId;

    private String publicationTitle;

    @NotNull
    @ManyToOne
    private ExportRequest exportRequest;

    @NotNull
    @Column(name = "item_order")
    private Long order;

    @OneToOne
    private Export rootExport;

    @NotNull
    @Enumerated(EnumType.STRING)
    private KrameriusModel model;
}
