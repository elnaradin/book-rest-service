package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.example.repository.impl.CategoryRepositoryImpl;
import org.example.repository.mapper.ResultSetMapper;
import org.example.servlet.dto.incoming.IncomingCategoryDto;
import org.example.servlet.mapper.CategoryMapper;
import org.example.sevice.CategoryService;
import org.example.sevice.impl.CategoryServiceImpl;
import org.mapstruct.factory.Mappers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = "/category")
public class CategoryServlet extends HttpServlet {
    CategoryService categoryService;
    CategoryMapper categoryMapper;
    ObjectMapper objectMapper;

    @Override
    public void init() {
        ResultSetMapper resultSetMapper = Mappers.getMapper(ResultSetMapper.class);
        ConnectionManager cm = new ConnectionManagerImpl();
        CategoryRepository categoryRepository = new CategoryRepositoryImpl(cm, resultSetMapper);
        categoryMapper = Mappers.getMapper(CategoryMapper.class);
        categoryService = new CategoryServiceImpl(categoryRepository);
        objectMapper = new ObjectMapper();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            IncomingCategoryDto newBook = objectMapper.readValue(req.getReader(), IncomingCategoryDto.class);
            categoryService.add(categoryMapper.map(newBook));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            handleException(resp, e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Category> categoryDTOS = categoryService.findAll();
            String valueAsString = objectMapper.writeValueAsString(categoryMapper.map(categoryDTOS));
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(valueAsString);
        } catch (Exception e) {
            handleException(resp, e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            IncomingCategoryDto incomingCategoryDto = objectMapper.readValue(req.getReader(), IncomingCategoryDto.class);
            if (incomingCategoryDto.getId() == null) {
                throw new RuntimeException("id must not be null");
            }
            categoryService.update(categoryMapper.map(incomingCategoryDto));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            handleException(resp, e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long id = Long.parseLong(req.getParameter("id"));
            categoryService.deleteById(id);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            handleException(resp, e);
        }
    }

    private static void handleException(HttpServletResponse resp, Exception e) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        try {
            resp.getWriter().write(e.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
