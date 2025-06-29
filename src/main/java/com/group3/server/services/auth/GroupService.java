package com.group3.server.services.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.group3.server.dtos.auth.GroupRequest;
import com.group3.server.dtos.auth.GroupResponse;
import com.group3.server.models.auth.Group;
import com.group3.server.models.auth.Permission;
import com.group3.server.repositories.auth.GroupRepository;
import com.group3.server.repositories.auth.PermissionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "GroupService")
@Transactional
public class GroupService {
    private final GroupRepository groupRepository;
    private final PermissionRepository permissionRepository;

    public List<GroupResponse> getAllGroupsNotAdmin() {
        return groupRepository
                .findByNameNot("ADMIN").stream().map(group -> new GroupResponse(group.getId(), group.getName(),
                        group.getDescription(), group.getPermissions().stream().toList()))
                .toList();
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .filter(p -> p.getId() != 20L && p.getId() != 2L)
                .toList();
    }

    public GroupResponse createGroup(GroupRequest request) {
        groupRepository.findByName(request.getName())
                .ifPresent(group -> {
                    throw new RuntimeException("Nhóm quyền với tên " + request.getName() + " đã tồn tại");
                });

        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.getPermissionIds()));

        Group group = new Group();
        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setPermissions(permissions);

        Group savedGroup = groupRepository.save(group);

        return new GroupResponse(savedGroup.getId(), savedGroup.getName(), savedGroup.getDescription(),
                permissions.stream().toList());
    }

    public GroupResponse updateGroup(Integer id, GroupRequest request) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm quyền với id: " + id));

        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.getPermissionIds()));
        if(id == 2 && !request.getName().equals("CUSTOMER")) {
            throw new RuntimeException("Không được thay đổi mã nhóm CUSTOMER");
        }

        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setPermissions(permissions);

        Group updatedGroup = groupRepository.save(group);

        return new GroupResponse(updatedGroup.getId(), updatedGroup.getName(), updatedGroup.getDescription(),
                permissions.stream().toList());

    }

    public void deleteGroup(Integer id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm quyền với id: " + id));
        if(group.getUsers() != null && !group.getUsers().isEmpty()) {
            throw new RuntimeException("Không thể xóa nhóm quyền này vì có người dùng đang sử dụng nhóm quyền này");
        }
        if(group.getName().equals("CUSTOMER")) {
            throw new RuntimeException("Không thể xóa nhóm quyền CUSTOMER");
        }
        groupRepository.delete(group);
    }

}
