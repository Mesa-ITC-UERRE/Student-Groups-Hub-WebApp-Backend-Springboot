package com.student_groups_api.model;

import com.student_groups_api.model.enums.PermissionRole;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "role_assignments",
        indexes = {
                @Index(name = "ix_role_assignments_group_term_role", columnList = "group_id,term_id,permission_role"),
                @Index(name = "ix_role_assignments_group_user", columnList = "group_id,user_id")
        }
)
public class RoleAssignment extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id")
    private GroupTerm term;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_role", nullable = false)
    private PermissionRole permissionRole;

    // “custom role name” that does NOT change permissions.
    @Column(name = "display_role")
    private String displayRole;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    public RoleAssignment() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() { return id; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    public GroupTerm getTerm() { return term; }
    public void setTerm(GroupTerm term) { this.term = term; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public PermissionRole getPermissionRole() { return permissionRole; }
    public void setPermissionRole(PermissionRole permissionRole) { this.permissionRole = permissionRole; }

    public String getDisplayRole() { return displayRole; }
    public void setDisplayRole(String displayRole) { this.displayRole = displayRole; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
}