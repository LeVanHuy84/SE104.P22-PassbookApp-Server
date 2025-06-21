package com.group3.server.mappers.auth;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.group3.server.dtos.auth.UserResponse;
import com.group3.server.models.auth.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(target = "groupName", source = "group.description")
    UserResponse toDTO(User entity);
    List<UserResponse> toDTOs(List<User> entities);
}
