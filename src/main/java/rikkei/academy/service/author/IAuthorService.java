package rikkei.academy.service.author;

import rikkei.academy.model.Author;
import rikkei.academy.service.IGenericService;

public interface IAuthorService extends IGenericService<Author> {
    Boolean existsByName(String name);
}
