package ru.aston.lepd.readingclubspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aston.lepd.readingclubspring.dto.ReaderDto;
import ru.aston.lepd.readingclubspring.service.ReaderService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;


@RestController
@RequestMapping("/readers")
public class ReaderController {


    private final ReaderService readerService;
    private static final String UPDATE_STRING = "Reader with id=%d was updated";
    private static final String DELETE_STRING = "Reader with id=%d was deleted";

    @Autowired
    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }




    @GetMapping()
    public ResponseEntity<List<ReaderDto>> getAll() {
        List<ReaderDto> readerDtoList = readerService.getAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(readerDtoList);
    }



    @GetMapping("/{readerId}")
    public ResponseEntity<ReaderDto> getById(@PathVariable Long readerId) {
        ReaderDto readerDto = readerService.getById(readerId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(readerDto);
    }



    @PostMapping
    public ResponseEntity<ReaderDto> save(@RequestBody ReaderDto readerDto) {
        ReaderDto savedReader = readerService.save(readerDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(savedReader);
    }



    @PutMapping("/{readerId}")
    public ResponseEntity<String> update(@RequestBody ReaderDto readerDto, @PathVariable Long readerId) {
        readerService.update(readerDto, readerId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(TEXT_PLAIN)
                .body(String.format(UPDATE_STRING, readerId));
    }



    @DeleteMapping("/{readerId}")
    public ResponseEntity<String> delete(@PathVariable Long readerId) {
        readerService.delete(readerId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(TEXT_PLAIN)
                .body(String.format(DELETE_STRING, readerId));
    }



}
