package rikkei.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rikkei.academy.model.Author;

public interface IAuthorRepository extends JpaRepository<Author, Long> {
    Boolean existsByName(String name);
}
