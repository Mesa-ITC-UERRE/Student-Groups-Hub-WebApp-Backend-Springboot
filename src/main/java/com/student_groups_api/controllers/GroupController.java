package com.student_groups_api.controllers;

import com.student_groups_api.dto.groups.GroupMemberResponse;
import com.student_groups_api.dto.groups.GroupResponse;
import com.student_groups_api.dto.groups.JoinGroupResponse;
import com.student_groups_api.services.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<List<GroupResponse>> getAll() {
        return ResponseEntity.ok(groupService.getAllActiveGroups());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<GroupResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(groupService.getBySlug(slug));
    }

    @PostMapping("/{groupId}/join")
    public ResponseEntity<JoinGroupResponse> requestJoin(@PathVariable UUID groupId) {
        return ResponseEntity.ok(groupService.requestJoin(groupId));
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMemberResponse>> getMembers(@PathVariable UUID groupId) {
        return ResponseEntity.ok(groupService.getApprovedMembers(groupId));
    }
}