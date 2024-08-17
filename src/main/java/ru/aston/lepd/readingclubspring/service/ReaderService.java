package ru.aston.lepd.readingclubspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.lepd.readingclubspring.dto.ReaderDto;
import ru.aston.lepd.readingclubspring.entity.Reader;
import ru.aston.lepd.readingclubspring.exception.NotFoundException;
import ru.aston.lepd.readingclubspring.repository.ReaderRepository;
import ru.aston.lepd.readingclubspring.util.CustomMapper;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class ReaderService {


    private final ReaderRepository readerRepository;
    private final CustomMapper mapper;
    private static final String NOT_FOUND = "There is no reader with id=%d in database";

    @Autowired
    public ReaderService(ReaderRepository readerRepository, CustomMapper mapper) {
        this.readerRepository = readerRepository;
        this.mapper = mapper;
    }





    public List<ReaderDto> getAll() {
        return readerRepository.findAll()
                .stream()
                .map(mapper::readerToReaderDto)
                .toList();
    }



    public ReaderDto getById(Long readerId) {
        return readerRepository.findById(readerId)
                .map(mapper::readerToReaderDto)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, readerId)));
    }



    public Reader getReaderById(Long readerId) {
        return readerRepository.findById(readerId)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, readerId)));
    }



    @Transactional
    public ReaderDto save(ReaderDto readerDto) {
        Reader reader = mapper.readerDtoToReader(readerDto);
        Reader savedReader = readerRepository.save(reader);
        return mapper.readerToReaderDto(savedReader);
    }



    @Transactional
    public ReaderDto update(ReaderDto readerDto, Long readerId) {
        Reader updating = getReaderById(readerId);
        Optional.ofNullable(readerDto.getName()).ifPresent(updating::setName);
        Optional.ofNullable(readerDto.getSurname()).ifPresent(updating::setSurname);
        Optional.ofNullable(readerDto.getPhone()).ifPresent(updating::setPhone);
        Optional.ofNullable(readerDto.getAddress()).ifPresent(updating::setAddress);
        return mapper.readerToReaderDto(updating);
    }



    @Transactional
    public void delete(Long readerId) {
        existsById(readerId);
        readerRepository.deleteById(readerId);
    }



    public boolean existsById(Long readerId) {
        boolean result = readerRepository.existsById(readerId);
        if (!result) throw new NotFoundException(String.format(NOT_FOUND, readerId));
        return true;
    }



}
