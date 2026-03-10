package com.student_groups_api.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "notifications",
        indexes = {
                @Index(name = "ix_notifications_user_read_created", columnList = "user_id,is_read,created_at")
        }
)
public class Notification extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "kind", nullable = false)
    private String kind; // "membership_request", "membership_decision", "new_event", etc.

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "body", columnDefinition = "text")
    private String body;

    @Column(name = "href")
    private String href; // deep link path

    @Column(name = "is_read", nullable = false)
    private boolean read = false;

    public Notification() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getKind() { return kind; }
    public void setKind(String kind) { this.kind = kind; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getHref() { return href; }
    public void setHref(String href) { this.href = href; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}