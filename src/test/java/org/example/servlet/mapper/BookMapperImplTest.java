package org.example.servlet.mapper;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Category;
import org.example.servlet.dto.incoming.IncomingBookDto;
import org.example.servlet.dto.outcoming.OutComingBookDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookMapperImplTest {

    private BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        bookMapper = Mappers.getMapper(BookMapper.class);
    }

    @Test
    void testMapEntityToDto() {
        Author author = new Author(1L, "Tolstoy");
        Category category = new Category(1L, "drama");
        Book book = new Book(1L, "drama", author, List.of(category));
        OutComingBookDTO dto = bookMapper.map(book);
        assertEquals(book.getId(), dto.getId());
        assertEquals(book.getTitle(), dto.getTitle());
        assertNotNull(book.getAuthor());
        assertEquals(1, book.getCategories().size());
    }

    @Test
    void testMapDtoToEntity() {
        IncomingBookDto dto = new IncomingBookDto(1L, "drama", 1L, List.of(1L, 2L));
        Book book = bookMapper.map(dto);
        assertEquals(dto.getId(), book.getId());
        assertEquals(dto.getTitle(), book.getTitle());
        assertNull(book.getAuthor());
        assertNull(book.getCategories());
    }

    @Test
    void testMapEntityListToDtoList() {
        Author author = new Author(1L, "Tolstoy");
        Category category = new Category(1L, "drama");
        Book book1 = new Book(1L, "drama", author, List.of(category));
        Book book2 = new Book(2L, "sci-fi", author, List.of(category));
        List<OutComingBookDTO> outComingBookDTOS = bookMapper.map(List.of(book1, book2));
        assertEquals(2, outComingBookDTOS.size());
    }
}