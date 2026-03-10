package com.student_groups_api.model;

import com.student_groups_api.model.enums.RequestStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "group_registration_requests",
        indexes = {
                @Index(name = "ix_group_reg_requests_status_created", columnList = "status,created_at"),
                @Index(name = "ix_group_reg_requests_requested_by", columnList = "requested_by_user_id,created_at")
        }
)
public class GroupRegistrationRequest extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by_user_id", nullable = false)
    private User requestedBy;

    @Column(name = "proposed_group_name", nullable = false)
    private String proposedGroupName;

    @Column(name = "proposed_description", columnDefinition = "text")
    private String proposedDescription;

    @Column(name = "contact_email", nullable = false)
    private String contactEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status = RequestStatus.pending;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by_user_id")
    private User reviewedBy; // platform admin

    @Column(name = "reviewed_at")
    private Instant reviewedAt;

    @Column(name = "decision_notes", columnDefinition = "text")
    private String decisionNotes;

    public GroupRegistrationRequest() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() { return id; }

    public User getRequestedBy() { return requestedBy; }
    public void setRequestedBy(User requestedBy) { this.requestedBy = requestedBy; }

    public String getProposedGroupName() { return proposedGroupName; }
    public void setProposedGroupName(String proposedGroupName) { this.proposedGroupName = proposedGroupName; }

    public String getProposedDescription() { return proposedDescription; }
    public void setProposedDescription(String proposedDescription) { this.proposedDescription = proposedDescription; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public User getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(User reviewedBy) { this.reviewedBy = reviewedBy; }

    public Instant getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(Instant reviewedAt) { this.reviewedAt = reviewedAt; }

    public String getDecisionNotes() { return decisionNotes; }
    public void setDecisionNotes(String decisionNotes) { this.decisionNotes = decisionNotes; }
}