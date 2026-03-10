package com.student_groups_api.model;

import com.student_groups_api.model.enums.TermStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "group_terms",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_group_terms_group_label", columnNames = {"group_id", "label"})
        }
)
public class GroupTerm extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(name = "label", nullable = false)
    private String label; // "2026-2027"

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TermStatus status = TermStatus.active;

    public GroupTerm() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() { return id; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public TermStatus getStatus() { return status; }
    public void setStatus(TermStatus status) { this.status = status; }
}