package rikkei.academy.service.comic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rikkei.academy.model.Category;
import rikkei.academy.model.Comic;
import rikkei.academy.service.IGenericService;

import java.util.List;

public interface IComicService extends IGenericService<Comic> {
    Boolean existsByName(String name);

}