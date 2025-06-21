package com.group3.server.dtos.Specification;

import org.springframework.data.jpa.domain.Specification;

import com.group3.server.dtos.Filter.UserFilter;
import com.group3.server.models.auth.User;

public class UserSpecification {
    public static Specification<User> withFilter(UserFilter filter) {
        return Specification
                .where(hasFullNameLike(filter.getFullname()))
                .and(hasCitizenId(filter.getCitizenID()))
                .and(hasActive(filter.getIsActive()))
                .and(hasGroupId(filter.getGroupId()));
    }

    private static Specification<User> hasFullNameLike(String fullname) {
        return (root, query, cb) -> {
            if (fullname == null || fullname.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("fullname")), "%" + fullname.toLowerCase() + "%");
        };
    }

    private static Specification<User> hasCitizenId(String citizenId) {
        return (root, query, cb) -> {
            if (citizenId == null || citizenId.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("citizenID"), citizenId);
        };
    }

    private static Specification<User> hasActive(Boolean isActive) {
        return (root, query, cb) -> {
            if(isActive == null) {
                return cb.conjunction();
            }
            if (isActive) {
                return cb.isTrue(root.get("isActive"));
            } else {
                return cb.isFalse(root.get("isActive"));
            }
        };
    }

    private static Specification<User> hasGroupId(Integer groupId) {
        return (root, query, cb) -> {
            if (groupId == null) {
                // Lọc tất cả user có group.name khác ADMIN
                return cb.notEqual(root.join("group").get("id"), 1L);
            }
            return cb.equal(root.join("group").get("id"), groupId);
        };
    }

}
