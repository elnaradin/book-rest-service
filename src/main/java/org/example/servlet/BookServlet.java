package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.model.Book;
import org.example.repository.AuthorRepository;
import org.example.repository.CategoryRepository;
import org.example.repository.impl.AuthorRepositoryImpl;
import org.example.repository.impl.BookRepositoryImpl;
import org.example.repository.impl.CategoryRepositoryImpl;
import org.example.repository.mapper.ResultSetMapper;
import org.example.servlet.dto.incoming.IncomingBookDto;
import org.example.servlet.mapper.BookMapper;
import org.example.sevice.BookService;
import org.example.sevice.impl.BookServiceImpl;
import org.mapstruct.factory.Mappers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = "/book")
public class BookServlet extends HttpServlet {
    BookService bookService;
    ObjectMapper objectMapper;
    BookMapper bookMapper;


    @Override
    public void init() {
        ResultSetMapper resultSetMapper = Mappers.getMapper(ResultSetMapper.class);
        ConnectionManager cm = new ConnectionManagerImpl();
        BookRepositoryImpl bookRepository = new BookRepositoryImpl(cm, resultSetMapper);
        bookMapper = Mappers.getMapper(BookMapper.class);
        CategoryRepository categoryRepository = new CategoryRepositoryImpl(cm, resultSetMapper);
        AuthorRepository authorRepository = new AuthorRepositoryImpl(cm, resultSetMapper);
        bookService = new BookServiceImpl(bookRepository, categoryRepository, authorRepository);
        objectMapper = new ObjectMapper();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            IncomingBookDto incomingBookDto = objectMapper.readValue(req.getReader(), IncomingBookDto.class);
            bookService.add(bookMapper.map(incomingBookDto), incomingBookDto.getAuthorId(), incomingBookDto.getCategoryIds());
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            handleException(resp, e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Book> books = bookService.getAll();
            String valueAsString = objectMapper.writeValueAsString(bookMapper.map(books));
            resp.setContentType("application/json");
            resp.getWriter().write(valueAsString);
        } catch (Exception e) {
            handleException(resp, e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            IncomingBookDto incomingBookDto = objectMapper.readValue(req.getReader(), IncomingBookDto.class);
            if (incomingBookDto.getId() == null) {
                throw new RuntimeException("id must not be null");
            }
            bookService.update(bookMapper.map(incomingBookDto), incomingBookDto.getAuthorId(), incomingBookDto.getCategoryIds());
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            handleException(resp, e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long id = Long.parseLong(req.getParameter("id"));
            bookService.deleteById(id);
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
            throw new RuntimeException(ex);
        }
    }
}
