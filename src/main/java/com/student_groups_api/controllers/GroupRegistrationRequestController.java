package com.student_groups_api.controllers;

import com.student_groups_api.dto.groups.CreateGroupRegistrationRequest;
import com.student_groups_api.dto.groups.GroupRegistrationRequestResponse;
import com.student_groups_api.services.GroupRegistrationRequestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/group-registration-requests")
public class GroupRegistrationRequestController {

    private final GroupRegistrationRequestService service;

    public GroupRegistrationRequestController(GroupRegistrationRequestService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<GroupRegistrationRequestResponse> create(
            @Valid @RequestBody CreateGroupRegistrationRequest request
    ) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<GroupRegistrationRequestResponse>> myRequests() {
        return ResponseEntity.ok(service.getMyRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupRegistrationRequestResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }
}