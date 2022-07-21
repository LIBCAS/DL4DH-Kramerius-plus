package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.monograph;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Object used to represent donators
 *
 * @author Filip Kollar
 */
@Getter
public class Donators {
    @JsonIgnore
    private List<String> donators;

    @JsonProperty("donator")
    public void unpackDonators(Object donator) {
        donators = new ArrayList<>();

        if (donator != null && donator.getClass().equals(String.class)) {
            donators.add((String) donator);
        } else if (donator instanceof List<?>) {
            donators.addAll(((List<?>) donator).stream()
                    .map(obj -> (String) obj)
                    .collect(Collectors.toList()));
        }
    }
}
