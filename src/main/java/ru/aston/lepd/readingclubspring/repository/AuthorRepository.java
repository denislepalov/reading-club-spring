package ru.aston.lepd.readingclubspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.aston.lepd.readingclubspring.entity.Author;

import java.util.List;


@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {


    List<Author> findAuthorsByBooks_Id(Long bookId);

    boolean existsById(Long authorId);

}
