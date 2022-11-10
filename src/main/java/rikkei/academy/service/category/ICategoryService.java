package rikkei.academy.service.category;

import rikkei.academy.model.Category;
import rikkei.academy.service.IGenericService;

import java.util.List;

public interface ICategoryService extends IGenericService<Category> {
    Boolean existsByName(String name);
}
