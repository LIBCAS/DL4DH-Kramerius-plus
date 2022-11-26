package cz.inqool.dl4dh.krameriusplus.api.publication.object;

import cz.inqool.dl4dh.krameriusplus.api.publication.ModsMetadata;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.PageDto;
import cz.inqool.dl4dh.krameriusplus.api.publication.paradata.EnrichmentParadata;
import cz.inqool.dl4dh.krameriusplus.api.publication.paradata.ExternalSystem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class PublicationDto extends DigitalObjectDto {

    private List<DigitalObjectDto> children = new ArrayList<>();

    private List<PageDto> pages = new ArrayList<>();

    private String title;

    private List<String> collections;

    private String policy;

    private ModsMetadata modsMetadata;

    private Map<ExternalSystem, EnrichmentParadata> paradata = new HashMap<>();

    private boolean pdf;

    private List<String> donator = new ArrayList<>();

    private Long pageCount;

    private String rootId;
}
