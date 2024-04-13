package org.example.servlet.dto.incoming;

import java.util.List;

public class IncomingBookDto {
    private Long id;
    private String title;
    private Long authorId;
    private List<Long> categoryIds;

    public IncomingBookDto(Long id, String title, Long authorId, List<Long> categoryIds) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.categoryIds = categoryIds;
    }

    public IncomingBookDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }
}

