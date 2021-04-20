package cz.inqool.dl4dh.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class UDPipeMetadata {

    private int position;

    private String lemma;

    private String uPosTag;

    private String xPosTag;

    private String feats;

    private String head;

    private String depRel;

    private String misc;
}
