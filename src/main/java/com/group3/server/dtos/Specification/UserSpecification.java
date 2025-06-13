package com.group3.server.dtos.Specification;

import org.springframework.data.jpa.domain.Specification;

import com.group3.server.dtos.Filter.UserFilter;
import com.group3.server.models.auth.User;

public class UserSpecification {
    public static Specification<User> withFilter(UserFilter filter) {
        return Specification
                .where(hasFullNameLike(filter.getFullname()))
                .and(hasCitizenId(filter.getCitizenId()));
    }

    private static Specification<User> hasFullNameLike(String fullname) {
        return (root, query, cb) -> {
            if (fullname == null || fullname.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("fullname")), "%" + fullname.toLowerCase() + "%");
        };
    }

    private static Specification<User> hasCitizenId(String citizenId) {
        return (root, query, cb) -> {
            if (citizenId == null || citizenId.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("citizenId"), citizenId);
        };
    }
}
