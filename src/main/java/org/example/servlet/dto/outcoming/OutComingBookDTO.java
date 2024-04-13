package org.example.servlet.dto.outcoming;

import java.util.List;

public class OutComingBookDTO {
    private Long id;
    private String title;
    private OutComingAuthorDTO author;
    private List<OutComingCategoryDTO> categories;


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

    public OutComingAuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(OutComingAuthorDTO author) {
        this.author = author;
    }

    public List<OutComingCategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<OutComingCategoryDTO> categories) {
        this.categories = categories;
    }
}

