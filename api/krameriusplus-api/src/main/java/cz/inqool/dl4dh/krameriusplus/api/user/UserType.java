package cz.inqool.dl4dh.krameriusplus.api.user;

import lombok.Getter;

@Getter
public enum UserType {

    USER("user"),
    ADMIN("dl4dh-admin");

    private final String name;

    UserType(String name) {
        this.name = name;
    }
}
