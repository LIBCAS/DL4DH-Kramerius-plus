package cz.inqool.dl4dh.krameriusplus.api.domain;

import cz.inqool.dl4dh.krameriusplus.api.user.UserDto;

public abstract class OwnedObjectCreateDto extends DatedObjectCreateDto {

    private UserDto owner;
}
