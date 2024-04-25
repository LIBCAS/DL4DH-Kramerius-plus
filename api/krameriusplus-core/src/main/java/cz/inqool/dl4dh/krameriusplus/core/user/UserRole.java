package cz.inqool.dl4dh.krameriusplus.core.user;

public enum UserRole {

    ADMIN("dl4dh-admin"),
    USER("user");

    private String roleName;

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
