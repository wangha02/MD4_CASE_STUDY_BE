package rikkei.academy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rikkei.academy.model.Category;
import rikkei.academy.model.Comic;

import java.util.List;

@Repository
public interface IComicRepository extends JpaRepository<Comic,Long> {
    Boolean existsByName(String name);
}
