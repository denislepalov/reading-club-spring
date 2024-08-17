package ru.aston.lepd.readingclubspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aston.lepd.readingclubspring.dto.AuthorDto;
import ru.aston.lepd.readingclubspring.service.AuthorService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;


@RestController
@RequestMapping("/authors")
public class AuthorController {


    private final AuthorService authorService;
    private static final String UPDATE_STRING = "Author with id=%d was updated";
    private static final String DELETE_STRING = "Author with id=%d was deleted";

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }




    @GetMapping()
    public ResponseEntity<List<AuthorDto>> getAll() {
        List<AuthorDto> authorDtoList = authorService.getAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(authorDtoList);
    }


    @GetMapping("/books/{bookId}")
    public ResponseEntity<List<AuthorDto>> getAllByBookId(@PathVariable Long bookId) {
        List<AuthorDto> authorDtoList = authorService.getAllByBookId(bookId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(authorDtoList);
    }


    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorDto> getById(@PathVariable Long authorId) {
        AuthorDto authorDto = authorService.getById(authorId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(authorDto);
    }


    @PostMapping
    public ResponseEntity<AuthorDto> save(@RequestBody AuthorDto authorDto) {
        AuthorDto savedAuthor = authorService.save(authorDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(savedAuthor);
    }


    @PutMapping("/{authorId}")
    public ResponseEntity<String> update(@RequestBody AuthorDto authorDto, @PathVariable Long authorId) {
        authorService.update(authorDto, authorId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(TEXT_PLAIN)
                .body(String.format(UPDATE_STRING, authorId));
    }


    @DeleteMapping("/{authorId}")
    public ResponseEntity<String> delete(@PathVariable Long authorId) {
        authorService.delete(authorId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(TEXT_PLAIN)
                .body(String.format(DELETE_STRING, authorId));
    }


}
