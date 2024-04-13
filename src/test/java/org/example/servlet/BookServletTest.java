package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Category;
import org.example.servlet.dto.incoming.IncomingBookDto;
import org.example.servlet.mapper.BookMapper;
import org.example.sevice.BookService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BookServletTest {
    private BookServlet bookServlet;
    private BookService bookServiceMock;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ObjectMapper objectMapperMock;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        bookServiceMock = mock(BookService.class);
        objectMapperMock = mock(ObjectMapper.class);
        bookServlet = new BookServlet() {
            @Override
            public void init() {
                bookService = bookServiceMock;
                bookMapper = Mappers.getMapper(BookMapper.class);
                objectMapper = objectMapperMock;
            }
        };
        bookServlet.init();
        when(request.getReader()).thenReturn(mock(BufferedReader.class));
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
        doNothing().when(writer).write(anyString());
    }

    @AfterEach
    void tearDown() {
        bookServlet.destroy();
    }

    @Test
    void testPost() throws ServletException, IOException {
        IncomingBookDto dto = new IncomingBookDto();
        dto.setId(1L);
        dto.setTitle("Leo");
        dto.setAuthorId(1L);
        dto.setCategoryIds(List.of(1L, 2L));
        when(objectMapperMock.readValue(any(BufferedReader.class), any(Class.class))).thenReturn(dto);
        bookServlet.doPost(request, response);
        verify(bookServiceMock, times(1)).add(any(Book.class), anyLong(), anyList());
    }

    @Test
    void testGet() throws ServletException, IOException {
        List<Book> books = List.of(new Book(1L, "Leo", new Author(), List.of(new Category())));
        when(bookServiceMock.getAll()).thenReturn(books);
        bookServlet.doGet(request, response);
        verify(bookServiceMock, times(1)).getAll();
    }

    @Test
    void testPut() throws ServletException, IOException {
        IncomingBookDto dto = new IncomingBookDto(1L, "Leo", 1L, List.of(1L, 2L));
        when(objectMapperMock.readValue(any(BufferedReader.class), any(Class.class))).thenReturn(dto);
        bookServlet.doPut(request, response);
        verify(bookServiceMock, times(1)).update(any(Book.class), anyLong(), anyList());
    }

    @Test
    void testDelete() throws ServletException, IOException {
        long id = 1;
        when(request.getParameter("id")).thenReturn(String.valueOf(id));
        bookServlet.doDelete(request, response);
        verify(bookServiceMock, times(1)).deleteById(id);
    }

    @Test
    void whenIdNull_then400() throws ServletException, IOException {
        IncomingBookDto dto = new IncomingBookDto(null, "Leo", 1L, null);
        when(objectMapperMock.readValue(any(BufferedReader.class), any(Class.class))).thenReturn(dto);
        bookServlet.doPut(request, response);
        verify(bookServiceMock, times(0)).update(any(Book.class), anyLong(), anyList());
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}