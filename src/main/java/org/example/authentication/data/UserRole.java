package org.example.authentication.data;

import org.apache.commons.lang3.StringUtils;

public enum UserRole {
    ROLE_ADMIN, ROLE_READER;

    public static UserRole fromText(String userRole) {
        for (UserRole role : UserRole.values()) {
            if (StringUtils.equalsIgnoreCase(role.name(), userRole)) {
                return role;
            }
        }
        throw new IllegalArgumentException(String.format("Invalid value '%s' for UserRole.", userRole));

    }
}
