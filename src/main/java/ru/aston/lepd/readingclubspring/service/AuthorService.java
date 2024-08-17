package ru.aston.lepd.readingclubspring.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.lepd.readingclubspring.dto.AuthorDto;
import ru.aston.lepd.readingclubspring.entity.Author;
import ru.aston.lepd.readingclubspring.exception.NotFoundException;
import ru.aston.lepd.readingclubspring.repository.AuthorRepository;
import ru.aston.lepd.readingclubspring.util.CustomMapper;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final CustomMapper mapper;
    private static final String NOT_FOUND = "There is no author with id=%d in database";


    @Autowired
    public AuthorService(AuthorRepository authorRepository, CustomMapper mapper) {
        this.authorRepository = authorRepository;
        this.mapper = mapper;
    }




    public AuthorDto getById(Long authorId) {
        return authorRepository.findById(authorId)
                .map(mapper::authorToAuthorDto)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, authorId)));
    }



    public Author getAuthorById(Long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, authorId)));
    }



    public List<AuthorDto> getAll() {
        return authorRepository.findAll()
                .stream()
                .map(mapper::authorToAuthorDto)
                .toList();
    }


    public List<AuthorDto> getAllByBookId(Long bookId) {
        return authorRepository.findAuthorsByBooks_Id(bookId)
                .stream()
                .map(mapper::authorToAuthorDto)
                .toList();
    }



    @Transactional
    public AuthorDto save(AuthorDto authorDto) {
        Author author = mapper.authorDtoToAuthor(authorDto);
        Author savedAuthor = authorRepository.save(author);
        return mapper.authorToAuthorDto(savedAuthor);
    }


    @Transactional
    public AuthorDto update(AuthorDto authorDto, Long authorId) {
        Author updating = getAuthorById(authorId);
        Optional.ofNullable(authorDto.getFullName()).ifPresent(updating::setFullName);
        Optional.ofNullable(authorDto.getPersonalInfo()).ifPresent(updating::setPersonalInfo);
        return mapper.authorToAuthorDto(updating);
    }


    @Transactional
    public void delete(Long authorId) {
        existsById(authorId);
        authorRepository.deleteById(authorId);
    }



    public boolean existsById(Long authorId) {
        boolean result = authorRepository.existsById(authorId);
        if (!result) throw new NotFoundException(String.format(NOT_FOUND, authorId));
        return true;
    }


}
