package org.example.servlet.mapper;

import org.example.model.Category;
import org.example.servlet.dto.incoming.IncomingCategoryDto;
import org.example.servlet.dto.outcoming.OutComingCategoryDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    Category map(IncomingCategoryDto categoryDTO);

    OutComingCategoryDTO map(Category category);

    List<OutComingCategoryDTO> map(List<Category> categories);
}
