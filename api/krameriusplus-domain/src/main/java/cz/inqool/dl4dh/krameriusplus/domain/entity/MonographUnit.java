package cz.inqool.dl4dh.krameriusplus.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Document(collection = "monograph_units")
public class MonographUnit extends Publication {

    private String partNumber;

    private String partTitle;

    private String donator;

    private String rootPid;

    @Transient
    private List<Page> pages = new ArrayList<>();
}
