package ru.aston.lepd.readingclubspring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.lepd.readingclubspring.dto.AuthorDto;
import ru.aston.lepd.readingclubspring.entity.Author;
import ru.aston.lepd.readingclubspring.entity.Book;
import ru.aston.lepd.readingclubspring.exception.NotFoundException;
import ru.aston.lepd.readingclubspring.repository.AuthorRepository;
import ru.aston.lepd.readingclubspring.util.CustomMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.aston.lepd.readingclubspring.Constants.*;


@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private CustomMapper mapper;
    @InjectMocks
    private AuthorService authorService;





    private Author getAuthor() {
        Author author = new Author();
        author.setId(1L);
        author.setFullName("Author1");
        author.setPersonalInfo("likes dogs");
        author.setBooks(List.of(new Book()));
        return author;
    }


    @Test
    public void getById_whenValidId_thenReturnAuthorDto() {
        final Long authorId = 1L;
        final Author author = getAuthor();
        doReturn(Optional.of(author)).when(authorRepository).findById(authorId);
        doReturn(AUTHOR_DTO_1).when(mapper).authorToAuthorDto(any(Author.class));

        AuthorDto actualResult = authorService.getById(authorId);

        verify(authorRepository).findById(authorId);
        verify(mapper).authorToAuthorDto(any(Author.class));
        assertEquals(AUTHOR_DTO_1, actualResult);
    }

    @Test
    public void getById_whenInvalidId_thenThrowException() {
        final Long authorId = 666L;
        doReturn(Optional.empty()).when(authorRepository).findById(authorId);

        assertThrows(NotFoundException.class, () -> authorService.getById(authorId));

        verify(authorRepository).findById(authorId);
        verify(mapper, never()).authorToAuthorDto(any(Author.class));
    }



    @Test
    public void getAuthorById_whenValidId_thenReturnAuthor() {
        final Long authorId = 1L;
        final Author author = getAuthor();
        doReturn(Optional.of(author)).when(authorRepository).findById(authorId);

        Author actualResult = authorService.getAuthorById(authorId);

        verify(authorRepository).findById(authorId);
        assertEquals(AUTHOR_1.getId(), actualResult.getId());
    }

    @Test
    public void getAuthorById_whenInvalidId_thenThrowException() {
        final Long authorId = 666L;
        doReturn(Optional.empty()).when(authorRepository).findById(authorId);

        assertThrows(NotFoundException.class, () -> authorService.getAuthorById(authorId));

        verify(authorRepository).findById(authorId);
    }



    @Test
    public void getAll_whenExist_thenReturnList() {
        doReturn(List.of(getAuthor(), getAuthor(), getAuthor())).when(authorRepository).findAll();
        doReturn(AUTHOR_DTO_1).when(mapper).authorToAuthorDto(any(Author.class));

        List<AuthorDto> actualResult = authorService.getAll();

        verify(authorRepository).findAll();
        verify(mapper, times(3)).authorToAuthorDto(any(Author.class));
        assertFalse(actualResult.isEmpty());
        assertEquals(3, actualResult.size());
    }

    @Test
    public void getAll_whenNotExist_thenReturnEmptyList() {
        doReturn(Collections.emptyList()).when(authorRepository).findAll();

        List<AuthorDto> actualResult = authorService.getAll();

        verify(authorRepository).findAll();
        verify(mapper, never()).authorToAuthorDto(any(Author.class));
        assertTrue(actualResult.isEmpty());
    }



    @Test
    public void getAllByBookId_whenExist_thenReturnList() {
        final Long bookId = 1L;

        doReturn(List.of(getAuthor(), getAuthor())).when(authorRepository).findAuthorsByBooks_Id(bookId);
        doReturn(AUTHOR_DTO_1).when(mapper).authorToAuthorDto(any(Author.class));

        List<AuthorDto> actualResult = authorService.getAllByBookId(bookId);

        verify(authorRepository).findAuthorsByBooks_Id(bookId);
        verify(mapper, times(2)).authorToAuthorDto(any(Author.class));
        assertFalse(actualResult.isEmpty());
        assertEquals(2, actualResult.size());
    }

    @Test
    public void getAllByBookId_whenNotExist_thenReturnEmptyList() {
        final Long bookId = 666L;

        doReturn(Collections.emptyList()).when(authorRepository).findAuthorsByBooks_Id(bookId);

        List<AuthorDto> actualResult = authorService.getAllByBookId(bookId);

        verify(authorRepository).findAuthorsByBooks_Id(bookId);
        verify(mapper, never()).authorToAuthorDto(any(Author.class));
        assertTrue(actualResult.isEmpty());
    }



    @Test
    public void save() {
        final Author author = new Author();
        author.setFullName("Author1");
        author.setPersonalInfo("likes dogs");
        doReturn(author).when(mapper).authorDtoToAuthor(AUTHOR_DTO_1);
        doReturn(SHORT_AUTHOR_1).when(authorRepository).save(author);
        doReturn(AUTHOR_DTO_1).when(mapper).authorToAuthorDto(SHORT_AUTHOR_1);

        AuthorDto actualResult = authorService.save(AUTHOR_DTO_1);

        verify(mapper).authorDtoToAuthor(AUTHOR_DTO_1);
        verify(authorRepository).save(author);
        verify(mapper).authorToAuthorDto(SHORT_AUTHOR_1);
        assertEquals(AUTHOR_DTO_1, actualResult);
    }



    @Test
    public void update_whenValidId_thenSuccess() {
        final Long authorId = 1L;
        Author author = getAuthor();
        doReturn(Optional.of(author)).when(authorRepository).findById(authorId);
        doReturn(AUTHOR_DTO_1).when(mapper).authorToAuthorDto(author);

        AuthorDto actualResult = authorService.update(AUTHOR_DTO_1, authorId);

        verify(authorRepository).findById(authorId);
        verify(mapper).authorToAuthorDto(author);
        assertEquals(AUTHOR_DTO_1, actualResult);
    }

    @Test
    public void update_whenInvalidId_thenTrowException() {
        final Long authorId = 666L;
        doReturn(Optional.empty()).when(authorRepository).findById(authorId);

        assertThrows(NotFoundException.class, () -> authorService.update(AUTHOR_DTO_1, authorId));

        verify(authorRepository).findById(authorId);
        verify(mapper, never()).authorToAuthorDto(AUTHOR_1);
    }



    @Test
    public void delete_whenValidId_thenTrue() {
        final Long authorId = 1L;
        doReturn(true).when(authorRepository).existsById(authorId);
        doNothing().when(authorRepository).deleteById(authorId);

        authorService.delete(authorId);

        verify(authorRepository).existsById(authorId);
        verify(authorRepository).deleteById(authorId);
    }

    @Test
    public void delete_whenInvalidId_thenThrowException() {
        final Long authorId = 666L;
        doReturn(false).when(authorRepository).existsById(authorId);

        assertThrows(NotFoundException.class, () -> authorService.delete(authorId));

        verify(authorRepository).existsById(authorId);
        verify(authorRepository, never()).deleteById(authorId);
    }



    @Test
    public void existsById_whenValidId_thenTrue() {
        final Long authorId = 1L;
        doReturn(true).when(authorRepository).existsById(authorId);

        boolean actualResult = authorService.existsById(authorId);

        assertTrue(actualResult);
    }

    @Test
    public void existsById_whenInvalidId_thenTrowException() {
        final Long authorId = 666L;
        doReturn(false).when(authorRepository).existsById(authorId);

        assertThrows(NotFoundException.class, () -> authorService.existsById(authorId));
    }



}