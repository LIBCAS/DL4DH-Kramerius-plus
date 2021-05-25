package cz.inqool.dl4dh.krameriusplus.domain.entity.page;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@EqualsAndHashCode
public class LinguisticMetadata {

    @Field("p")
    private int position;

    @Field("l")
    private String lemma;

    @Field("u")
    private String uPosTag;

    @Field("x")
    private String xPosTag;

    @Field("f")
    private String feats;

    @Field("h")
    private String head;

    @Field("d")
    private String depRel;

    @Field("m")
    private String misc;

    // if UPosTag == (NOUN or ADJ), it returns the CASE extracted from feats field, otherwise returns null
    public String getCase() {
        return extractAttribute("Case", "NOUN", "ADJ");
    }

    // if UPosTag == (NOUN or ADJ), it returns the GENDER extracted from feats field, otherwise returns null
    public String getGender() {
        return extractAttribute("Gender", "NOUN", "ADJ");
    }

    // if UPosTag == (NOUN or ADJ or VERB), it returns the NUMBER extracted from feats field, otherwise returns null
    public String getNumber() {
        return extractAttribute("Number", "NOUN", "ADJ", "VERB");
    }

    // if UPosTag == VERB, it returns the PERSON extracted from feats field, otherwise returns null
    public String getPerson() {
        return extractAttribute("Person", "VERB");
    }

    private String extractAttribute(String attributeName, String... inUPosTags) {
        try {
            if (Arrays.stream(inUPosTags).anyMatch(tag -> uPosTag.equals(tag))) {
                if (feats != null && !feats.isEmpty()) {
                    String[] feats = this.feats.split("\\|");
                    for (String feat : feats) {
                        String att = feat.split("=")[0];
                        String value = feat.split("=")[1];

                        if (att.equals(attributeName)) {
                            return value;
                        }
                    }
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
