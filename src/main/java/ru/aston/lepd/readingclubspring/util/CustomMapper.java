package ru.aston.lepd.readingclubspring.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.aston.lepd.readingclubspring.dto.AuthorDto;
import ru.aston.lepd.readingclubspring.dto.BookDto;
import ru.aston.lepd.readingclubspring.dto.ReaderDto;
import ru.aston.lepd.readingclubspring.entity.Author;
import ru.aston.lepd.readingclubspring.entity.Book;
import ru.aston.lepd.readingclubspring.entity.Reader;

import java.util.ArrayList;
import java.util.List;


@Mapper
public interface CustomMapper {


    ReaderDto readerToReaderDto(Reader reader);
    Reader readerDtoToReader(ReaderDto readerDto);

    AuthorDto authorToAuthorDto(Author author);
    Author authorDtoToAuthor(AuthorDto authorDto);




    @Mapping(source = "authors", target = "authorIds")
    @Mapping(source = "reader", target = "readerId")
    BookDto bookToBookDto(Book book);

    default List<Long> mapAuthors(List<Author> authors) {
        return authors.stream()
                .map(Author::getId)
                .toList();
    }

    default Long mapReader(Reader reader) {
        return reader.getId();
    }




    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "reader", ignore = true)
    Book bookDtoToBook(BookDto bookDto);



}
