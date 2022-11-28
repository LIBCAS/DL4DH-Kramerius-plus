package cz.inqool.dl4dh.krameriusplus.api.user;

import cz.inqool.dl4dh.krameriusplus.api.domain.DatedObjectDto;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDto extends DatedObjectDto {

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private Set<String> authorities = new HashSet<>();
}
