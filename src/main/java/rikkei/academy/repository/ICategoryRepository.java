package rikkei.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rikkei.academy.model.Category;

import java.util.List;

public interface ICategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByNameContaining(String name);

}
