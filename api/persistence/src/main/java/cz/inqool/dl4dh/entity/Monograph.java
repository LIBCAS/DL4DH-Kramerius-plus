package cz.inqool.dl4dh.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Object representing a Monograph. Monographs contain pages directly.
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Document(collection = "monographs")
public class Monograph extends Publication {

    @Transient
    private List<Page> pages = new ArrayList<>();
}
