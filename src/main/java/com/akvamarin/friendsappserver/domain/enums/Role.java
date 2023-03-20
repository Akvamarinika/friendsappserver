package com.akvamarin.friendsappserver.domain.enums;

import lombok.experimental.FieldNameConstants;
import org.springframework.security.core.GrantedAuthority;


@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum Role implements GrantedAuthority {
    @FieldNameConstants.Include ADMIN,
    @FieldNameConstants.Include USER,
    @FieldNameConstants.Include UNKNOWN,
    @FieldNameConstants.Include MODERATOR;

    @Override
    public String getAuthority() {
        return this.name();
    }

    @Override
    public String toString() {
        return name();
    }
}
