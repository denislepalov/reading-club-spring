package ru.aston.lepd.readingclubspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.aston.lepd.readingclubspring.entity.Reader;


@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {

    boolean existsById(Long readerId);

}
