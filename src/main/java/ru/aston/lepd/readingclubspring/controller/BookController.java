package ru.aston.lepd.readingclubspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aston.lepd.readingclubspring.dto.BookDto;
import ru.aston.lepd.readingclubspring.service.BookService;
import java.util.List;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;


@RestController
@RequestMapping("/books")
public class BookController {


    private final BookService bookService;
    private static final String UPDATE_STRING = "Book with id=%d was updated";
    private static final String DELETE_STRING = "Book with id=%d was deleted";
    private static final String DELETE_ALL_BY_READER_ID_STRING = "All books by reader with id=%d was deleted";

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }





    @GetMapping("/{bookId}")
    public ResponseEntity<BookDto> getById(@PathVariable Long bookId) {
        BookDto bookDto = bookService.getById(bookId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(bookDto);
    }



    @GetMapping()
    public ResponseEntity<List<BookDto>> getAll() {
        List<BookDto> bookDtoList = bookService.getAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(bookDtoList);
    }



    @GetMapping("/reader/{readerId}")
    public ResponseEntity<List<BookDto>> getAllByReaderId(@PathVariable Long readerId) {
        List<BookDto> bookDtoList = bookService.getAllByReaderId(readerId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(bookDtoList);
    }



    @GetMapping("/authors/{authorId}")
    public ResponseEntity<List<BookDto>> getAllByAuthorId(@PathVariable Long authorId) {
        List<BookDto> bookDtoList = bookService.getAllByAuthorId(authorId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(bookDtoList);
    }



    @PostMapping
    public ResponseEntity<BookDto> save(@RequestBody BookDto bookDto) {
        BookDto savedBook = bookService.save(bookDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(savedBook);
    }



    @PutMapping("/{bookId}")
    public ResponseEntity<String> update(@RequestBody BookDto bookDto, @PathVariable Long bookId) {
        bookService.update(bookDto, bookId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(TEXT_PLAIN)
                .body(String.format(UPDATE_STRING, bookId));
    }



    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> delete(@PathVariable Long bookId) {
        bookService.delete(bookId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(TEXT_PLAIN)
                .body(String.format(DELETE_STRING, bookId));
    }


    @DeleteMapping("/reader/{readerId}")
    public ResponseEntity<String> deleteAllByReaderId(@PathVariable Long readerId) {
        bookService.deleteAllByReaderId(readerId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(TEXT_PLAIN)
                .body(String.format(DELETE_ALL_BY_READER_ID_STRING, readerId));
    }



}
