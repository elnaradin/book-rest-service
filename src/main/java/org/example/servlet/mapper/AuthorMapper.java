package org.example.servlet.mapper;

import org.example.model.Author;
import org.example.servlet.dto.incoming.IncomingAuthorDto;
import org.example.servlet.dto.outcoming.OutComingAuthorDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AuthorMapper {
    Author map(IncomingAuthorDto authorDTO);

    OutComingAuthorDTO map(Author author);

    List<OutComingAuthorDTO> map(List<Author> authors);
}
