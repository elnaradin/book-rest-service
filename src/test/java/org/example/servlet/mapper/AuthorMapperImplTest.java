package org.example.servlet.mapper;

import org.example.model.Author;
import org.example.servlet.dto.incoming.IncomingAuthorDto;
import org.example.servlet.dto.outcoming.OutComingAuthorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthorMapperImplTest {

    private AuthorMapper authorMapper;

    @BeforeEach
    void setUp() {
        authorMapper = Mappers.getMapper(AuthorMapper.class);
    }

    @Test
    void testMapEntityToDto() {
        Author author = new Author(1L, "Tolstoy");
        OutComingAuthorDTO dto = authorMapper.map(author);
        assertEquals(author.getId(), dto.getId());
        assertEquals(author.getName(), dto.getName());
    }

    @Test
    void testMapDtoToEntity() {
        IncomingAuthorDto dto = new IncomingAuthorDto();
        dto.setId(1L);
        dto.setName("Tolstoy");
        Author author = authorMapper.map(dto);
        assertEquals(dto.getId(), author.getId());
        assertEquals(dto.getName(), author.getName());
    }

    @Test
    void testMapEntityListToDtoList() {
        Author author1 = new Author(1L, "Tolstoy");
        Author author2 = new Author(2L, "Dostoyevsky");
        List<OutComingAuthorDTO> outComingAuthorDTOS = authorMapper.map(List.of(author1, author2));
        assertEquals(2, outComingAuthorDTOS.size());
    }
}