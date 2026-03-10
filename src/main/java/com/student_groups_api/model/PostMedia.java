package com.student_groups_api.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "post_media",
        indexes = {
                @Index(name = "ix_post_media_post_sort", columnList = "post_id,sort_order")
        }
)
public class PostMedia extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "media_type", nullable = false)
    private String mediaType = "image";

    @Column(name = "sort_order", nullable = false)
    private int sortOrder = 0;

    public PostMedia() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() { return id; }

    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}