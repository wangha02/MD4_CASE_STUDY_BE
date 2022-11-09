package rikkei.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rikkei.academy.model.Comic;

public interface IComicRepository extends JpaRepository<Comic,Long> {
    Boolean existsByName(String name);
}
