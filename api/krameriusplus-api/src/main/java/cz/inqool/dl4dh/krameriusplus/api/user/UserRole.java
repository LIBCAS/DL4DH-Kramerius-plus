package cz.inqool.dl4dh.krameriusplus.api.user;

import lombok.Getter;

@Getter
public enum UserRole {

    ADMIN(RoleNames.ADMIN),
    USER(RoleNames.USER);

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public static UserRole fromRoleName(String roleName) {
        if (ADMIN.roleName.equals(roleName)) {
            return ADMIN;
        }

        return USER;
    }
}
