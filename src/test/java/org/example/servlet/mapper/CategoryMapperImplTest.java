package org.example.servlet.mapper;

import org.example.model.Category;
import org.example.servlet.dto.incoming.IncomingCategoryDto;
import org.example.servlet.dto.outcoming.OutComingCategoryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CategoryMapperImplTest {
    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        categoryMapper = Mappers.getMapper(CategoryMapper.class);
    }

    @Test
    void testMapEntityToDto() {
        Category category = new Category(1L, "drama");
        OutComingCategoryDTO dto = categoryMapper.map(category);
        assertEquals(category.getId(), dto.getId());
        assertEquals(category.getName(), dto.getName());
    }

    @Test
    void testMapDtoToEntity() {
        IncomingCategoryDto dto = new IncomingCategoryDto();
        dto.setId(1L);
        dto.setName("drama");
        Category category = categoryMapper.map(dto);
        assertEquals(dto.getId(), category.getId());
        assertEquals(dto.getName(), category.getName());
    }

    @Test
    void testMapEntityListToDtoList() {
        Category category1 = new Category(1L, "drama");
        Category category2 = new Category(2L, "sci-fi");
        List<OutComingCategoryDTO> outComingCategoryDTOS = categoryMapper.map(List.of(category1, category2));
        assertEquals(2, outComingCategoryDTOS.size());
    }
}