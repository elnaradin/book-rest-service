package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.model.Author;
import org.example.repository.impl.AuthorRepositoryImpl;
import org.example.repository.mapper.ResultSetMapper;
import org.example.servlet.dto.incoming.IncomingAuthorDto;
import org.example.servlet.mapper.AuthorMapper;
import org.example.sevice.AuthorService;
import org.example.sevice.impl.AuthorServiceImpl;
import org.mapstruct.factory.Mappers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/author")
public class AuthorServlet extends HttpServlet {
    AuthorService authorService;
    AuthorMapper authorMapper;
    ObjectMapper objectMapper;

    @Override
    public void init() {
        ResultSetMapper resultSetMapper = Mappers.getMapper(ResultSetMapper.class);
        ConnectionManager cm = new ConnectionManagerImpl();
        AuthorRepositoryImpl authorRepository = new AuthorRepositoryImpl(cm, resultSetMapper);
        authorService = new AuthorServiceImpl(authorRepository);
        authorMapper = Mappers.getMapper(AuthorMapper.class);
        objectMapper = new ObjectMapper();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            IncomingAuthorDto authorDTO = objectMapper.readValue(req.getReader(), IncomingAuthorDto.class);
            authorService.add(authorMapper.map(authorDTO));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            handleException(resp, e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Author> books = authorService.getAll();
        try {
            String valueAsString = objectMapper.writeValueAsString(authorMapper.map(books));
            resp.setContentType("application/json");
            resp.getWriter().write(valueAsString);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            handleException(resp, e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            IncomingAuthorDto incomingAuthorDto = objectMapper.readValue(req.getReader(), IncomingAuthorDto.class);
            if (incomingAuthorDto.getId() == null) {
                throw new RuntimeException("id must not be null");
            }
            authorService.update(authorMapper.map(incomingAuthorDto));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            handleException(resp, e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long id = Long.parseLong(req.getParameter("id"));
            authorService.deleteById(id);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            handleException(resp, e);
        }
    }

    private static void handleException(HttpServletResponse resp, Exception e) {
        e.printStackTrace();
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        try {
            resp.getWriter().write(e.getMessage());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
