package com.student_groups_api.model;

import com.student_groups_api.model.enums.RsvpStatus;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "event_rsvps",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_event_rsvps_event_user", columnNames = {"event_id", "user_id"})
        },
        indexes = {
                @Index(name = "ix_event_rsvps_user_status", columnList = "user_id,status")
        }
)
public class EventRsvp extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RsvpStatus status = RsvpStatus.going;

    public EventRsvp() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() { return id; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public RsvpStatus getStatus() { return status; }
    public void setStatus(RsvpStatus status) { this.status = status; }
}