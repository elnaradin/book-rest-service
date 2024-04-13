package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Author;
import org.example.servlet.dto.incoming.IncomingAuthorDto;
import org.example.servlet.mapper.AuthorMapper;
import org.example.sevice.AuthorService;
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

import static org.mockito.Mockito.*;

class AuthorServletTest {
    private AuthorServlet authorServlet;
    private AuthorService authorServiceMock;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ObjectMapper objectMapperMock;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        authorServiceMock = mock(AuthorService.class);
        objectMapperMock = mock(ObjectMapper.class);
        authorServlet = new AuthorServlet() {
            @Override
            public void init() {
                authorService = authorServiceMock;
                authorMapper = Mappers.getMapper(AuthorMapper.class);
                objectMapper = objectMapperMock;
            }
        };
        authorServlet.init();
        when(request.getReader()).thenReturn(mock(BufferedReader.class));
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
        doNothing().when(writer).write(anyString());
    }

    @AfterEach
    void tearDown() {
        authorServlet.destroy();
    }

    @Test
    void testPost() throws ServletException, IOException {
        IncomingAuthorDto dto = new IncomingAuthorDto();
        dto.setName("Leo");
        when(objectMapperMock.readValue(any(BufferedReader.class), any(Class.class))).thenReturn(dto);
        authorServlet.doPost(request, response);
        verify(authorServiceMock, times(1)).add(any(Author.class));
    }

    @Test
    void testGet() throws ServletException, IOException {
        List<Author> authors = List.of(new Author(1L, "Leo"), new Author(2L, "Dmitriy"));
        when(authorServiceMock.getAll())
                .thenReturn(authors);
        authorServlet.doGet(request, response);
        verify(authorServiceMock, times(1)).getAll();
    }

    @Test
    void testPut() throws ServletException, IOException {
        IncomingAuthorDto dto = new IncomingAuthorDto();
        dto.setId(1L);
        dto.setName("Leo");
        when(objectMapperMock.readValue(any(BufferedReader.class), any(Class.class))).thenReturn(dto);
        authorServlet.doPut(request, response);
        verify(authorServiceMock, times(1)).update(any(Author.class));
    }

    @Test
    void whenIdNull_then400() throws ServletException, IOException {
        IncomingAuthorDto dto = new IncomingAuthorDto();
        dto.setName("Leo");
        when(objectMapperMock.readValue(any(BufferedReader.class), any(Class.class))).thenReturn(dto);
        authorServlet.doPut(request, response);
        verify(authorServiceMock, times(0)).update(any(Author.class));
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDelete() throws ServletException, IOException {
        long id = 1;
        when(request.getParameter("id")).thenReturn(String.valueOf(id));
        authorServlet.doDelete(request, response);
        verify(authorServiceMock, times(1)).deleteById(id);
    }
}