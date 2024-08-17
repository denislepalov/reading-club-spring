package ru.aston.lepd.readingclubspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.aston.lepd.readingclubspring.entity.Author;
import ru.aston.lepd.readingclubspring.entity.Book;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, Long>  {

    List<Book> findAllByReaderId(Long readerId);

    List<Book> findAllByAuthors_Id(Long authorId);

    void deleteAllByReaderId(Long readerId);

    boolean existsById(Long bookId);

}
