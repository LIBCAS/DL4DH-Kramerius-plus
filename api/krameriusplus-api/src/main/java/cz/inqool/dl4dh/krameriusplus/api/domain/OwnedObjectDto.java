package cz.inqool.dl4dh.krameriusplus.api.domain;

import cz.inqool.dl4dh.krameriusplus.api.user.UserDto;

public abstract class OwnedObjectDto extends DatedObjectDto {

    private UserDto owner;
}
