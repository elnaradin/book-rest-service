package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Category;
import org.example.servlet.dto.incoming.IncomingCategoryDto;
import org.example.servlet.mapper.CategoryMapper;
import org.example.sevice.CategoryService;
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

class CategoryServletTest {
    private CategoryServlet categoryServlet;
    private CategoryService categoryServiceMock;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ObjectMapper objectMapperMock;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        categoryServiceMock = mock(CategoryService.class);
        objectMapperMock = mock(ObjectMapper.class);
        categoryServlet = new CategoryServlet() {
            @Override
            public void init() {
                categoryService = categoryServiceMock;
                categoryMapper = Mappers.getMapper(CategoryMapper.class);
                objectMapper = objectMapperMock;
            }
        };
        categoryServlet.init();
        when(request.getReader()).thenReturn(mock(BufferedReader.class));
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
        doNothing().when(writer).write(anyString());
    }

    @AfterEach
    void tearDown() {
        categoryServlet.destroy();
    }

    @Test
    void testPost() throws ServletException, IOException {
        IncomingCategoryDto dto = new IncomingCategoryDto();
        dto.setName("drama");
        when(objectMapperMock.readValue(any(BufferedReader.class), any(Class.class))).thenReturn(dto);
        categoryServlet.doPost(request, response);
        verify(categoryServiceMock, times(1)).add(any(Category.class));
    }

    @Test
    void testGet() throws ServletException, IOException {
        List<Category> categorys = List.of(new Category(1L, "drama", List.of()), new Category(2L, "comedy", List.of()));
        when(categoryServiceMock.findAll())
                .thenReturn(categorys);
        categoryServlet.doGet(request, response);
        verify(categoryServiceMock, times(1)).findAll();
    }

    @Test
    void testPut() throws ServletException, IOException {
        IncomingCategoryDto dto = new IncomingCategoryDto();
        dto.setId(1L);
        dto.setName("drama");
        when(objectMapperMock.readValue(any(BufferedReader.class), any(Class.class))).thenReturn(dto);
        categoryServlet.doPut(request, response);
        verify(categoryServiceMock, times(1)).update(any(Category.class));
    }

    @Test
    void testDelete() throws ServletException, IOException {
        long id = 1;
        when(request.getParameter("id")).thenReturn(String.valueOf(id));
        categoryServlet.doDelete(request, response);
        verify(categoryServiceMock, times(1)).deleteById(id);
    }

    @Test
    void whenIdNull_then400() throws ServletException, IOException {
        IncomingCategoryDto dto = new IncomingCategoryDto();
        dto.setName("Leo");
        when(objectMapperMock.readValue(any(BufferedReader.class), any(Class.class))).thenReturn(dto);
        categoryServlet.doPut(request, response);
        verify(categoryServiceMock, times(0)).update(any(Category.class));
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}