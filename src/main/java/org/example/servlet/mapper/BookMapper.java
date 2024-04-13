package org.example.servlet.mapper;

import org.example.model.Book;
import org.example.servlet.dto.incoming.IncomingBookDto;
import org.example.servlet.dto.outcoming.OutComingBookDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {
    Book map(IncomingBookDto bookDTO);

    OutComingBookDTO map(Book book);

    List<OutComingBookDTO> map(List<Book> books);
}
