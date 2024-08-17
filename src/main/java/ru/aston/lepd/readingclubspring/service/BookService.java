package ru.aston.lepd.readingclubspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.lepd.readingclubspring.dto.BookDto;
import ru.aston.lepd.readingclubspring.entity.Author;
import ru.aston.lepd.readingclubspring.entity.Book;
import ru.aston.lepd.readingclubspring.entity.Reader;
import ru.aston.lepd.readingclubspring.exception.NotFoundException;
import ru.aston.lepd.readingclubspring.repository.AuthorRepository;
import ru.aston.lepd.readingclubspring.repository.BookRepository;
import ru.aston.lepd.readingclubspring.repository.ReaderRepository;
import ru.aston.lepd.readingclubspring.util.CustomMapper;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class BookService {


    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final ReaderService readerService;
    private final CustomMapper mapper;
    private static final String NOT_FOUND = "There is no book with id=%d in database";

    @Autowired
    public BookService(BookRepository bookRepository, AuthorService authorService, ReaderService readerService, CustomMapper mapper) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.readerService = readerService;
        this.mapper = mapper;
    }


    public BookDto getById(Long bookId) {
        return bookRepository.findById(bookId)
                .map(mapper::bookToBookDto)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, bookId)));
    }


    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, bookId)));
    }


    public List<BookDto> getAll() {
        return bookRepository.findAll()
                .stream()
                .map(mapper::bookToBookDto)
                .toList();
    }


    public List<BookDto> getAllByReaderId(Long readerId) {
        return bookRepository.findAllByReaderId(readerId)
                .stream()
                .map(mapper::bookToBookDto)
                .toList();
    }


    public List<BookDto> getAllByAuthorId(Long authorId) {
        return bookRepository.findAllByAuthors_Id(authorId)
                .stream()
                .map(mapper::bookToBookDto)
                .toList();
    }


    @Transactional
    public BookDto save(BookDto bookDto) {
        Book book = mapper.bookDtoToBook(bookDto);
        book.setAuthors(bookDto.getAuthorIds().stream()
                .map(authorService::getAuthorById)
                .toList());
        book.setReader(readerService.getReaderById(bookDto.getReaderId()));
        Book savedBook = bookRepository.save(book);
        return mapper.bookToBookDto(savedBook);
    }


    @Transactional
    public BookDto update(BookDto bookDto, Long bookId) {
        Book updating = getBookById(bookId);
        Optional.ofNullable(bookDto.getTitle()).ifPresent(updating::setTitle);
        Optional.ofNullable(bookDto.getInventoryNumber()).ifPresent(updating::setInventoryNumber);
        if (!bookDto.getAuthorIds().isEmpty()) {
            updating.setAuthors(bookDto.getAuthorIds().stream()
                    .map(authorService::getAuthorById)
                    .toList());
        }
        if (bookDto.getReaderId() != null) {
            updating.setReader(readerService.getReaderById(bookDto.getReaderId()));
        }
        return mapper.bookToBookDto(updating);
    }


    @Transactional
    public void delete(Long bookId) {
        existsById(bookId);
        bookRepository.deleteById(bookId);
    }


    @Transactional
    public void deleteAllByReaderId(Long readerId) {
        readerService.existsById(readerId);
        bookRepository.deleteAllByReaderId(readerId);
    }


    public boolean existsById(Long bookId) {
        boolean result = bookRepository.existsById(bookId);
        if (!result) throw new NotFoundException(String.format(NOT_FOUND, bookId));
        return true;
    }


}
